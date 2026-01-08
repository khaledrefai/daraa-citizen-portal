/**
 * منصة خدمة المواطن - محافظة درعا
 * Main Application JavaScript
 */

const state = {
  currentUser: null,
  selectedCategory: 'الكل',
  selectedService: null,
  otpPhone: null,
  services: [],
  chatbotOpen: false,
};

const elements = {
  servicesList: document.getElementById('servicesList'),
  serviceDetails: document.getElementById('serviceDetails'),
  categoryFilters: document.getElementById('categoryFilters'),
  filterContainer: document.getElementById('filterContainer'),
  filterToggle: document.getElementById('filterToggle'),
  filterDropdown: document.getElementById('filterDropdown'),
  filterClose: document.getElementById('filterClose'),
  activeFilterLabel: document.getElementById('activeFilterLabel'),
  filterChips: document.getElementById('filterChips'),
  heroStats: document.getElementById('heroStats'),
  complaintsPanel: document.getElementById('complaintsPanel'),
  requestsPanel: document.getElementById('requestsPanel'),
  suggestionsPanel: document.getElementById('suggestionsPanel'),
  violationsPanel: document.getElementById('violationsPanel'),
  authModal: document.getElementById('authModal'),
  toast: document.getElementById('toast'),
  loginForm: document.getElementById('loginForm'),
  registerForm: document.getElementById('registerForm'),
  otpForm: document.getElementById('otpForm'),
  userActions: document.querySelector('.user-actions'),
  // Chatbot elements
  chatbot: document.getElementById('chatbot'),
  chatbotTrigger: document.getElementById('chatbotTrigger'),
  chatbotWindow: document.getElementById('chatbotWindow'),
  chatbotClose: document.getElementById('chatbotClose'),
  chatbotMessages: document.getElementById('chatbotMessages'),
  chatbotForm: document.getElementById('chatbotForm'),
  chatbotSuggestions: document.getElementById('chatbotSuggestions'),
};

// ====================================
// User Actions & Authentication
// ====================================

const updateUserActions = () => {
  if (!elements.userActions) return;

  if (state.currentUser) {
    elements.userActions.innerHTML = `
      <div class="badge">
        <svg viewBox="0 0 24 24" aria-hidden="true"><path fill="currentColor" d="M12 12a5 5 0 1 0-5-5 5 5 0 0 0 5 5Zm0 2c-4.33 0-8 2.17-8 5v1h16v-1c0-2.83-3.67-5-8-5Z"/></svg>
        <span>${state.currentUser.fullName}</span>
      </div>
      <button class="btn btn-outline" id="logoutBtn">
        <svg viewBox="0 0 24 24" width="18" height="18"><path fill="currentColor" d="M17 7l-1.41 1.41L18.17 11H8v2h10.17l-2.58 2.58L17 17l5-5zM4 5h8V3H4c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h8v-2H4V5z"/></svg>
        تسجيل الخروج
      </button>
    `;
    elements.userActions.querySelector('#logoutBtn').addEventListener('click', () => {
      state.currentUser = null;
      showToast('تم تسجيل الخروج بنجاح');
      updateUserActions();
      loadComplaints();
      loadRequests();
    });
  } else {
    elements.userActions.innerHTML = `
      <button class="btn btn-outline" id="loginTrigger">
        <svg viewBox="0 0 24 24" width="18" height="18"><path fill="currentColor" d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/></svg>
        تسجيل الدخول
      </button>
      <button class="btn btn-primary" id="registerTrigger">
        <svg viewBox="0 0 24 24" width="18" height="18"><path fill="currentColor" d="M15 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm-9-2V7H4v3H1v2h3v3h2v-3h3v-2H6zm9 4c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/></svg>
        إنشاء حساب
      </button>
    `;
    initAuthTriggers();
  }
};

const showToast = (message, duration = 3500) => {
  const toastMessage = elements.toast.querySelector('.toast__message');
  if (toastMessage) {
    toastMessage.textContent = message;
  } else {
    elements.toast.textContent = message;
  }
  elements.toast.classList.add('show');
  setTimeout(() => elements.toast.classList.remove('show'), duration);
};

const toggleModal = (open = true) => {
  if (open) {
    elements.authModal.classList.add('open');
    elements.authModal.setAttribute('aria-hidden', 'false');
    document.body.style.overflow = 'hidden';
  } else {
    elements.authModal.classList.remove('open');
    elements.authModal.setAttribute('aria-hidden', 'true');
    document.body.style.overflow = '';
    switchAuthTab('login');
    state.otpPhone = null;
  }
};

const switchAuthTab = tab => {
  const tabs = elements.authModal.querySelectorAll('.modal__tab');
  const forms = elements.authModal.querySelectorAll('[data-tab-content]');

  tabs.forEach(btn => btn.classList.toggle('active', btn.dataset.tab === tab));
  forms.forEach(form => {
    form.classList.toggle('hidden', form.dataset.tabContent !== tab);
  });

  const subtitle = {
    login: 'سجّل الدخول للوصول لكافة الخدمات',
    register: 'أنشئ حساباً جديداً لتفعيل الخدمات الإلكترونية',
    otp: 'أدخل كلمة المرور لمرة واحدة المرسلة إلى هاتفك',
  };
  document.getElementById('authSubtitle').textContent = subtitle[tab];
};

// ====================================
// Hero Stats
// ====================================

const renderHeroStats = services => {
  const stats = [
    { 
      label: 'خدمات متاحة إلكترونياً', 
      value: services.filter(s => s.online).length,
      icon: '<svg viewBox="0 0 24 24" width="24" height="24"><path fill="currentColor" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/></svg>'
    },
    { 
      label: 'تصنيفات الخدمات', 
      value: new Set(services.map(s => s.category)).size,
      icon: '<svg viewBox="0 0 24 24" width="24" height="24"><path fill="currentColor" d="M12 2l-5.5 9h11L12 2zm0 3.84L13.93 9h-3.87L12 5.84zM17.5 13c-2.49 0-4.5 2.01-4.5 4.5s2.01 4.5 4.5 4.5 4.5-2.01 4.5-4.5-2.01-4.5-4.5-4.5zm0 7a2.5 2.5 0 0 1 0-5 2.5 2.5 0 0 1 0 5zM3 21.5h8v-8H3v8zm2-6h4v4H5v-4z"/></svg>'
    },
    { 
      label: 'زمن الاستجابة المتوسط', 
      value: '48 ساعة',
      icon: '<svg viewBox="0 0 24 24" width="24" height="24"><path fill="currentColor" d="M11.99 2C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8zm.5-13H11v6l5.25 3.15.75-1.23-4.5-2.67z"/></svg>'
    },
  ];
  
  elements.heroStats.innerHTML = stats
    .map(
      stat => `
      <div class="stat">
        <span>${stat.label}</span>
        <strong>${stat.value}</strong>
      </div>
    `,
    )
    .join('');
};

// ====================================
// Filter System
// ====================================

const renderFilters = services => {
  const categories = ['الكل', ...new Set(services.map(service => service.category))];
  
  elements.categoryFilters.innerHTML = categories
    .map(
      category => `
      <button class="filter ${state.selectedCategory === category ? 'active' : ''}" data-category="${category}">
        ${category}
      </button>
    `,
    )
    .join('');
    
  // Update active filter label
  elements.activeFilterLabel.textContent = state.selectedCategory === 'الكل' ? 'جميع التصنيفات' : state.selectedCategory;
  
  // Render filter chips
  renderFilterChips();
};

const renderFilterChips = () => {
  if (state.selectedCategory === 'الكل') {
    elements.filterChips.innerHTML = '';
    return;
  }
  
  elements.filterChips.innerHTML = `
    <div class="filter-chip">
      <span>${state.selectedCategory}</span>
      <button onclick="clearFilter()" aria-label="إزالة الفلتر">×</button>
    </div>
  `;
};

const clearFilter = () => {
  state.selectedCategory = 'الكل';
  renderFilters(state.services);
  renderServices(state.services);
  renderServiceDetails(null);
};

// Make clearFilter globally accessible
window.clearFilter = clearFilter;

const initFilterDropdown = () => {
  // Toggle dropdown
  elements.filterToggle?.addEventListener('click', () => {
    elements.filterContainer.classList.toggle('open');
  });
  
  // Close button
  elements.filterClose?.addEventListener('click', () => {
    elements.filterContainer.classList.remove('open');
  });
  
  // Close on outside click
  document.addEventListener('click', (e) => {
    if (!elements.filterContainer?.contains(e.target)) {
      elements.filterContainer?.classList.remove('open');
    }
  });
  
  // Filter selection
  elements.categoryFilters?.addEventListener('click', event => {
    if (event.target.matches('.filter')) {
      state.selectedCategory = event.target.dataset.category;
      renderFilters(state.services);
      renderServices(state.services);
      renderServiceDetails(null);
      elements.filterContainer.classList.remove('open');
    }
  });
};

// ====================================
// Services
// ====================================

const renderServices = services => {
  const filtered = state.selectedCategory === 'الكل' 
    ? services 
    : services.filter(service => service.category === state.selectedCategory);

  elements.servicesList.innerHTML = filtered
    .map(
      service => `
      <article class="service-card ${state.selectedService === service.id ? 'active' : ''}" data-service-id="${service.id}">
        <h4>${service.title}</h4>
        <p>${service.description}</p>
        <div class="taglist">
          ${service.tags.map(tag => `<span>${tag}</span>`).join('')}
        </div>
      </article>
    `,
    )
    .join('');

  if (!filtered.length) {
    elements.servicesList.innerHTML = `
      <div class="empty-state">
        <div class="empty-state__icon">
          <svg viewBox="0 0 24 24" width="48" height="48"><path fill="currentColor" opacity="0.4" d="M15.5 14h-.79l-.28-.27A6.471 6.471 0 0 0 16 9.5 6.5 6.5 0 1 0 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"/></svg>
        </div>
        <h4>لا توجد خدمات</h4>
        <p>لا توجد خدمات ضمن هذا التصنيف حالياً.</p>
      </div>
    `;
  }
};

const renderServiceDetails = async serviceId => {
  if (!serviceId) {
    elements.serviceDetails.innerHTML = `
      <div class="empty-state">
        <div class="empty-state__icon">
          <svg viewBox="0 0 24 24" width="64" height="64"><path fill="currentColor" opacity="0.3" d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z"/></svg>
        </div>
        <h4>اختر خدمة للاطلاع على تفاصيلها</h4>
        <p>اضغط على أي خدمة من القائمة لاستعراض الوصف، مسار العمل، وقنوات التواصل.</p>
      </div>
    `;
    return;
  }

  try {
    const service = await MockApi.getServiceById(serviceId);
    state.selectedService = serviceId;

    const workflowList = service.workflow.map((step, i) => `
      <li>
        <span class="step-number">${i + 1}</span>
        ${step}
      </li>
    `).join('');

    elements.serviceDetails.innerHTML = `
      <div class="detail-header">
        <h4>${service.title}</h4>
        <span class="tag">${service.category}</span>
      </div>
      <p>${service.description}</p>
      <h5>مسار العمل</h5>
      <ul class="workflow-list">${workflowList}</ul>
      <h5>قنوات التواصل</h5>
      <ul class="contact-list">
        <li>
          <svg viewBox="0 0 24 24" width="18" height="18"><path fill="currentColor" d="M6.62 10.79c1.44 2.83 3.76 5.14 6.59 6.59l2.2-2.2c.27-.27.67-.36 1.02-.24 1.12.37 2.33.57 3.57.57.55 0 1 .45 1 1V20c0 .55-.45 1-1 1-9.39 0-17-7.61-17-17 0-.55.45-1 1-1h3.5c.55 0 1 .45 1 1 0 1.25.2 2.45.57 3.57.11.35.03.74-.25 1.02l-2.2 2.2z"/></svg>
          ${service.contact.phone}
        </li>
        <li>
          <svg viewBox="0 0 24 24" width="18" height="18"><path fill="currentColor" d="M20 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z"/></svg>
          ${service.contact.email}
        </li>
        <li>
          <svg viewBox="0 0 24 24" width="18" height="18"><path fill="currentColor" d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5a2.5 2.5 0 0 1 0-5 2.5 2.5 0 0 1 0 5z"/></svg>
          ${service.contact.address}
        </li>
      </ul>
      ${
        service.online
          ? '<button class="btn btn-primary" id="applyService">التقديم على الخدمة إلكترونياً</button>'
          : '<p class="empty-state" style="margin-top: 1.5rem; padding: 1rem;">هذه الخدمة تتطلب الحضور الشخصي حالياً.</p>'
      }
    `;

    elements.serviceDetails.querySelector('#applyService')?.addEventListener('click', handleApplyService);
  } catch (error) {
    showToast(error.message);
  }
};

// ====================================
// Citizen Panels
// ====================================

const statusLabels = {
  pending: 'بانتظار المراجعة',
  'in-progress': 'قيد المعالجة',
  completed: 'منجزة',
  rejected: 'مرفوضة',
};

const renderComplaints = (items = []) => {
  elements.complaintsPanel.innerHTML = `
    <h4>
      <svg viewBox="0 0 24 24" width="24" height="24"><path fill="currentColor" d="M20 2H4c-1.1 0-1.99.9-1.99 2L2 22l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm-7 12h-2v-2h2v2zm0-4h-2V6h2v4z"/></svg>
      سجل الشكاوى
    </h4>
    <form id="complaintForm" class="form-grid">
      <label>
        <span>موضوع الشكوى</span>
        <input type="text" name="subject" required placeholder="أدخل موضوع الشكوى" />
      </label>
      <label>
        <span>التصنيف</span>
        <input type="text" name="category" placeholder="مثال: مياه، كهرباء، طرق" required />
      </label>
      <label>
        <span>تفاصيل الشكوى</span>
        <textarea name="details" required placeholder="اشرح تفاصيل الشكوى بشكل واضح..."></textarea>
      </label>
      <button class="btn btn-primary" type="submit">
        <svg viewBox="0 0 24 24" width="20" height="20"><path fill="currentColor" d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z"/></svg>
        إرسال الشكوى
      </button>
    </form>
    ${
      items.length
        ? `
        <table class="table">
          <thead>
            <tr>
              <th>المعرف</th>
              <th>الموضوع</th>
              <th>التصنيف</th>
              <th>الحالة</th>
              <th>آخر تحديث</th>
            </tr>
          </thead>
          <tbody>
            ${items
              .map(
                item => `
                  <tr>
                    <td><code>${item.id}</code></td>
                    <td>${item.subject}</td>
                    <td>${item.category}</td>
                    <td><span class="status-tag ${item.status}">${statusLabels[item.status]}</span></td>
                    <td>${item.updates[item.updates.length - 1]?.note || '-'}</td>
                  </tr>
                `,
              )
              .join('')}
          </tbody>
        </table>
      `
        : '<p class="empty-state" style="margin-top: 2rem;">لم تقم بتسجيل أي شكوى بعد.</p>'
    }
  `;

  elements.complaintsPanel.classList.add('active');
  elements.complaintsPanel.querySelector('#complaintForm').addEventListener('submit', async event => {
    event.preventDefault();
    if (!state.currentUser) return requireAuth();

    const formData = new FormData(event.target);
    const payload = Object.fromEntries(formData.entries());

    try {
      await MockApi.submitComplaint(state.currentUser.id, payload);
      showToast('تم تسجيل الشكوى بنجاح');
      loadComplaints();
      event.target.reset();
    } catch (error) {
      showToast(error.message);
    }
  });
};

const renderRequests = (items = []) => {
  elements.requestsPanel.innerHTML = `
    <h4>
      <svg viewBox="0 0 24 24" width="24" height="24"><path fill="currentColor" d="M19 3h-4.18C14.4 1.84 13.3 1 12 1c-1.3 0-2.4.84-2.82 2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 0c.55 0 1 .45 1 1s-.45 1-1 1-1-.45-1-1 .45-1 1-1zm2 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z"/></svg>
      متابعة الطلبات
    </h4>
    <form id="requestForm" class="form-grid">
      <label>
        <span>موضوع الطلب</span>
        <input type="text" name="subject" required placeholder="أدخل موضوع الطلب" />
      </label>
      <label>
        <span>الجهة المعنية</span>
        <input type="text" name="department" required placeholder="مثال: مديرية التربية" />
      </label>
      <label>
        <span>تفاصيل الطلب</span>
        <textarea name="details" required placeholder="اشرح تفاصيل طلبك..."></textarea>
      </label>
      <button class="btn btn-primary" type="submit">
        <svg viewBox="0 0 24 24" width="20" height="20"><path fill="currentColor" d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z"/></svg>
        إرسال الطلب
      </button>
    </form>
    ${
      items.length
        ? `
        <table class="table">
          <thead>
            <tr>
              <th>المعرف</th>
              <th>الموضوع</th>
              <th>الجهة</th>
              <th>الحالة</th>
              <th>تاريخ التقديم</th>
            </tr>
          </thead>
          <tbody>
            ${items
              .map(
                item => `
                  <tr>
                    <td><code>${item.id}</code></td>
                    <td>${item.subject}</td>
                    <td>${item.department}</td>
                    <td><span class="status-tag ${item.status}">${statusLabels[item.status]}</span></td>
                    <td>${item.createdAt}</td>
                  </tr>
                `,
              )
              .join('')}
          </tbody>
        </table>
      `
        : '<p class="empty-state" style="margin-top: 2rem;">لم يتم إرسال أي طلب حتى الآن.</p>'
    }
  `;

  elements.requestsPanel.classList.add('active');
  elements.requestsPanel.querySelector('#requestForm').addEventListener('submit', async event => {
    event.preventDefault();
    if (!state.currentUser) return requireAuth();

    const payload = Object.fromEntries(new FormData(event.target).entries());

    try {
      await MockApi.submitRequest(state.currentUser.id, payload);
      showToast('تم إرسال الطلب بنجاح');
      loadRequests();
      event.target.reset();
    } catch (error) {
      showToast(error.message);
    }
  });
};

const renderSuggestions = async () => {
  const items = await MockApi.getSuggestions();

  elements.suggestionsPanel.innerHTML = `
    <h4>
      <svg viewBox="0 0 24 24" width="24" height="24"><path fill="currentColor" d="M9 21c0 .55.45 1 1 1h4c.55 0 1-.45 1-1v-1H9v1zm3-19C8.14 2 5 5.14 5 9c0 2.38 1.19 4.47 3 5.74V17c0 .55.45 1 1 1h6c.55 0 1-.45 1-1v-2.26c1.81-1.27 3-3.36 3-5.74 0-3.86-3.14-7-7-7z"/></svg>
      خدمة الاقتراحات
    </h4>
    <form id="suggestionForm" class="form-grid">
      <label>
        <span>عنوان الاقتراح</span>
        <input type="text" name="title" required placeholder="اقتراح لتحسين..." />
      </label>
      <label>
        <span>وصف الاقتراح</span>
        <textarea name="body" required placeholder="اشرح اقتراحك بالتفصيل..."></textarea>
      </label>
      <button class="btn btn-primary" type="submit">
        <svg viewBox="0 0 24 24" width="20" height="20"><path fill="currentColor" d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z"/></svg>
        إرسال الاقتراح
      </button>
    </form>
    ${
      items.length
        ? `
        <table class="table">
          <thead>
            <tr>
              <th>العنوان</th>
              <th>الوصف</th>
              <th>التاريخ</th>
            </tr>
          </thead>
          <tbody>
            ${items
              .map(
                item => `
                  <tr>
                    <td><strong>${item.title}</strong></td>
                    <td>${item.body}</td>
                    <td>${item.createdAt}</td>
                  </tr>
                `,
              )
              .join('')}
          </tbody>
        </table>
      `
        : '<p class="empty-state" style="margin-top: 2rem;">لا توجد اقتراحات بعد، كن أول من يشارك رأيه.</p>'
    }
  `;

  elements.suggestionsPanel.classList.add('active');
  elements.suggestionsPanel.querySelector('#suggestionForm').addEventListener('submit', async event => {
    event.preventDefault();
    const payload = Object.fromEntries(new FormData(event.target).entries());
    try {
      await MockApi.submitSuggestion(payload);
      showToast('شكراً لمساهمتك!');
      renderSuggestions();
      event.target.reset();
    } catch (error) {
      showToast(error.message);
    }
  });
};

const renderViolations = async () => {
  const items = await MockApi.getViolations();
  const total = items.reduce((sum, item) => sum + item.amount, 0);

  elements.violationsPanel.innerHTML = `
    <h4>
      <svg viewBox="0 0 24 24" width="24" height="24"><path fill="currentColor" d="M11 15h2v2h-2v-2zm0-8h2v6h-2V7zm.99-5C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8z"/></svg>
      المخالفات والذمم
    </h4>
    <p style="color: var(--text-secondary); margin-bottom: 1.5rem;">يمكنك استعراض المخالفات المسجلة وسدادها إلكترونياً عند توفر خدمة الدفع.</p>
    ${
      items.length
        ? `
        <table class="table">
          <thead>
            <tr>
              <th>الرقم</th>
              <th>النوع</th>
              <th>الوصف</th>
              <th>القيمة (ل.س)</th>
              <th>الحالة</th>
              <th>التاريخ</th>
            </tr>
          </thead>
          <tbody>
            ${items
              .map(
                item => `
                  <tr>
                    <td><code>${item.id}</code></td>
                    <td>${item.type}</td>
                    <td>${item.description}</td>
                    <td><strong>${item.amount.toLocaleString('ar-SY')}</strong></td>
                    <td>${item.status}</td>
                    <td>${item.issuedAt}</td>
                  </tr>
                `,
              )
              .join('')}
          </tbody>
        </table>
        <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 1.5rem; padding: 1rem; background: var(--green-50); border-radius: var(--radius-md);">
          <span style="font-weight: 600;">إجمالي الذمم المستحقة</span>
          <strong style="font-size: 1.25rem; color: var(--green-700);">${total.toLocaleString('ar-SY')} ل.س</strong>
        </div>
      `
        : '<p class="empty-state" style="margin-top: 2rem;">لا توجد مخالفات مسجلة. ممتاز!</p>'
    }
  `;

  elements.violationsPanel.classList.add('active');
};

const requireAuth = () => {
  showToast('الرجاء تسجيل الدخول لإتمام هذه العملية');
  toggleModal(true);
};

const handleApplyService = () => {
  if (!state.currentUser) {
    requireAuth();
    return;
  }
  showToast('تم تهيئة طلبك للخدمة، سيتم تحويلك لواجهة التقديم قريباً');
};

const loadComplaints = async () => {
  if (!state.currentUser) {
    renderComplaints();
    return;
  }
  const items = await MockApi.getComplaints(state.currentUser.id);
  renderComplaints(items);
};

const loadRequests = async () => {
  if (!state.currentUser) {
    renderRequests();
    return;
  }
  const items = await MockApi.getRequests(state.currentUser.id);
  renderRequests(items);
};

const initPanels = async () => {
  await loadComplaints();
  await loadRequests();
  await renderSuggestions();
  await renderViolations();
};

// ====================================
// Chatbot - حوراني
// ====================================

const chatbotResponses = {
  'كيف أقدم شكوى؟': `لتقديم شكوى جديدة، اتبع الخطوات التالية:
1. سجّل الدخول إلى حسابك (أو أنشئ حساباً جديداً)
2. انتقل إلى قسم "خدمة الشكاوى" 
3. املأ نموذج الشكوى بالتفاصيل المطلوبة
4. اضغط على "إرسال الشكوى"

ستحصل على رقم متابعة فوري وسيتم الرد خلال 48 ساعة.`,
  
  'ما هي الخدمات المتاحة؟': `تتوفر لدينا عدة تصنيفات من الخدمات:
• خدمات قنصلية (تصديق الوثائق)
• خدمات سجل النفوس (البيان العائلي)
• خدمات المصالح العقارية
• خدمات مديرية التربية
• خدمات مؤسسة المياه
• خدمات الشؤون الاجتماعية

يمكنك استعراض جميع الخدمات من قسم "الخدمات الإلكترونية" أعلاه.`,

  'كيف أتابع طلبي؟': `لمتابعة طلبك:
1. سجّل الدخول إلى حسابك
2. انتقل إلى قسم "خدمة الطلبات"
3. ستجد جدولاً بجميع طلباتك مع حالة كل طلب

حالات الطلب:
• بانتظار المراجعة: تم استلام الطلب
• قيد المعالجة: يتم العمل على طلبك
• منجزة: تم إنجاز الطلب`,

  'أوقات العمل': `ساعات العمل الرسمية:
📅 الأحد - الخميس: 8:00 صباحاً - 3:00 مساءً

🚨 خدمة الطوارئ متاحة 24/7

📞 للتواصل: 015-123456
📧 البريد: citizen@daraa.gov.sy`,

  'default': `شكراً لتواصلك! أنا حوراني، مساعدك الذكي.

يمكنني مساعدتك في:
• الاستفسار عن الخدمات المتاحة
• شرح كيفية تقديم الشكاوى والطلبات
• معرفة أوقات العمل وطرق التواصل

للمزيد من المساعدة، تواصل مع الدعم على: 015-123456`
};

const initChatbot = () => {
  // Toggle chatbot window
  elements.chatbotTrigger?.addEventListener('click', () => {
    state.chatbotOpen = !state.chatbotOpen;
    elements.chatbot.classList.toggle('open', state.chatbotOpen);
    elements.chatbotWindow?.setAttribute('aria-hidden', !state.chatbotOpen);
  });
  
  // Close chatbot
  elements.chatbotClose?.addEventListener('click', () => {
    state.chatbotOpen = false;
    elements.chatbot.classList.remove('open');
    elements.chatbotWindow?.setAttribute('aria-hidden', 'true');
  });
  
  // Suggestion buttons
  elements.chatbotSuggestions?.addEventListener('click', (e) => {
    const btn = e.target.closest('.chatbot__suggestion');
    if (btn) {
      const query = btn.dataset.query;
      sendChatMessage(query);
    }
  });
  
  // Form submission
  elements.chatbotForm?.addEventListener('submit', (e) => {
    e.preventDefault();
    const input = e.target.querySelector('input[name="message"]');
    const message = input.value.trim();
    if (message) {
      sendChatMessage(message);
      input.value = '';
    }
  });
};

const sendChatMessage = (message) => {
  // Add user message
  addChatMessage(message, 'user');
  
  // Hide suggestions after first message
  if (elements.chatbotSuggestions) {
    elements.chatbotSuggestions.style.display = 'none';
  }
  
  // Simulate typing delay
  setTimeout(() => {
    const response = chatbotResponses[message] || chatbotResponses['default'];
    addChatMessage(response, 'bot');
  }, 800);
};

const addChatMessage = (text, sender) => {
  const messageDiv = document.createElement('div');
  messageDiv.className = `chatbot__message chatbot__message--${sender}`;
  
  const avatarIcon = sender === 'bot' 
    ? '<svg viewBox="0 0 24 24" width="20" height="20"><path fill="currentColor" d="M12 2a2 2 0 0 1 2 2c0 .74-.4 1.39-1 1.73V7h1a7 7 0 0 1 7 7h1a1 1 0 0 1 1 1v3a1 1 0 0 1-1 1h-1v1a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-1H2a1 1 0 0 1-1-1v-3a1 1 0 0 1 1-1h1a7 7 0 0 1 7-7h1V5.73c-.6-.34-1-.99-1-1.73a2 2 0 0 1 2-2M7.5 13A2.5 2.5 0 0 0 5 15.5 2.5 2.5 0 0 0 7.5 18a2.5 2.5 0 0 0 2.5-2.5A2.5 2.5 0 0 0 7.5 13m9 0a2.5 2.5 0 0 0-2.5 2.5 2.5 2.5 0 0 0 2.5 2.5 2.5 2.5 0 0 0 2.5-2.5 2.5 2.5 0 0 0-2.5-2.5z"/></svg>'
    : '<svg viewBox="0 0 24 24" width="20" height="20"><path fill="currentColor" d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/></svg>';
  
  // Convert newlines to <br> for display
  const formattedText = text.replace(/\n/g, '<br>');
  
  messageDiv.innerHTML = `
    <div class="chatbot__message-avatar">${avatarIcon}</div>
    <div class="chatbot__message-content">
      <p>${formattedText}</p>
    </div>
  `;
  
  elements.chatbotMessages?.appendChild(messageDiv);
  
  // Scroll to bottom
  if (elements.chatbotMessages) {
    elements.chatbotMessages.scrollTop = elements.chatbotMessages.scrollHeight;
  }
};

// ====================================
// Auth & Navigation
// ====================================

const initAuthTriggers = () => {
  const loginTrigger = document.getElementById('loginTrigger');
  const registerTrigger = document.getElementById('registerTrigger');
  
  loginTrigger?.addEventListener('click', () => {
    switchAuthTab('login');
    toggleModal(true);
  });

  registerTrigger?.addEventListener('click', () => {
    switchAuthTab('register');
    toggleModal(true);
  });
};

const initAuth = () => {
  // Close modal on overlay click or close button
  elements.authModal?.addEventListener('click', event => {
    if (event.target.dataset.close !== undefined || event.target.classList.contains('modal__overlay')) {
      toggleModal(false);
    }
  });

  elements.authModal?.querySelectorAll('.modal__tab').forEach(tab => {
    tab.addEventListener('click', () => switchAuthTab(tab.dataset.tab));
  });

  elements.loginForm?.addEventListener('submit', async event => {
    event.preventDefault();
    const { phone, password } = Object.fromEntries(new FormData(event.target).entries());
    try {
      const user = await MockApi.login(phone, password);
      state.currentUser = user;
      toggleModal(false);
      showToast(`مرحباً بك ${user.fullName}!`);
      loadComplaints();
      loadRequests();
      updateUserActions();
    } catch (error) {
      showToast(error.message);
    }
  });

  elements.registerForm?.addEventListener('submit', async event => {
    event.preventDefault();
    const payload = Object.fromEntries(new FormData(event.target).entries());
    try {
      const { phone, otp } = await MockApi.register(payload);
      state.otpPhone = phone;
      switchAuthTab('otp');
      const hint = elements.otpForm?.querySelector('.otp__hint');
      if (hint) {
        hint.textContent = `تم إرسال رمز التفعيل إلى الرقم ${phone}. (رمز تجريبي: ${otp})`;
      }
    } catch (error) {
      showToast(error.message);
    }
  });

  elements.otpForm?.addEventListener('submit', async event => {
    event.preventDefault();
    const { otp } = Object.fromEntries(new FormData(event.target).entries());
    if (!state.otpPhone) return;

    try {
      const user = await MockApi.verifyOtp(state.otpPhone, otp);
      state.currentUser = user;
      state.otpPhone = null;
      switchAuthTab('login');
      toggleModal(false);
      showToast('تم تفعيل الحساب بنجاح! مرحباً بك في المنصة.');
      loadComplaints();
      loadRequests();
      updateUserActions();
    } catch (error) {
      showToast(error.message);
    }
  });
};

const initNavigationCards = () => {
  document.querySelectorAll('.citizen-card').forEach(card => {
    card.addEventListener('click', () => {
      const target = card.dataset.target;
      const panel = document.getElementById(`${target}Panel`);
      if (!panel) return;
      
      // Show panel
      panel.classList.add('active');
      
      // Smooth scroll
      setTimeout(() => {
        panel.scrollIntoView({ behavior: 'smooth', block: 'start' });
      }, 100);
    });
  });
};

const initServices = async () => {
  try {
    const services = await MockApi.getServices();
    state.services = services;
    renderHeroStats(services);
    renderFilters(services);
    renderServices(services);
    renderServiceDetails(state.selectedService);

    elements.servicesList?.addEventListener('click', event => {
      const card = event.target.closest('[data-service-id]');
      if (!card) return;
      const serviceId = card.dataset.serviceId;
      renderServiceDetails(serviceId);
      renderServices(state.services);
    });
  } catch (error) {
    showToast('تعذر تحميل الخدمات حالياً');
    console.error(error);
  }
};

const initCTAButtons = () => {
  document.getElementById('exploreServices')?.addEventListener('click', () => {
    document.getElementById('servicesSection')?.scrollIntoView({ behavior: 'smooth', block: 'start' });
  });

  document.getElementById('contactSupport')?.addEventListener('click', () => {
    // Open chatbot instead of calling
    state.chatbotOpen = true;
    elements.chatbot?.classList.add('open');
    elements.chatbotWindow?.setAttribute('aria-hidden', 'false');
  });
};

// ====================================
// Initialize Application
// ====================================

const init = async () => {
  updateUserActions();
  initAuth();
  initFilterDropdown();
  initNavigationCards();
  initCTAButtons();
  initChatbot();
  await initServices();
  await initPanels();
};

window.addEventListener('DOMContentLoaded', init);
