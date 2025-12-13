const state = {
  currentUser: null,
  selectedCategory: 'الكل',
  selectedService: null,
  otpPhone: null,
};

const elements = {
  servicesList: document.getElementById('servicesList'),
  serviceDetails: document.getElementById('serviceDetails'),
  categoryFilters: document.getElementById('categoryFilters'),
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
};

const updateUserActions = () => {
  if (!elements.userActions) return;

  if (state.currentUser) {
    elements.userActions.innerHTML = `
      <div class="badge">
        <svg viewBox="0 0 24 24" aria-hidden="true"><path fill="currentColor" d="M12 12a5 5 0 1 0-5-5 5 5 0 0 0 5 5Zm0 2c-4.33 0-8 2.17-8 5v1h16v-1c0-2.83-3.67-5-8-5Z"/></svg>
        <span>${state.currentUser.fullName}</span>
      </div>
      <button class="btn btn-outline" id="logoutBtn">تسجيل الخروج</button>
    `;
    elements.userActions.querySelector('#logoutBtn').addEventListener('click', () => {
      state.currentUser = null;
      showToast('تم تسجيل الخروج بنجاح.');
      updateUserActions();
      loadComplaints();
      loadRequests();
    });
  } else {
    elements.userActions.innerHTML = `
      <button class="btn btn-outline" id="loginTrigger">تسجيل الدخول</button>
      <button class="btn" id="registerTrigger">إنشاء حساب</button>
    `;
    initAuthTriggers();
  }
};

const showToast = (message, duration = 3000) => {
  elements.toast.textContent = message;
  elements.toast.classList.add('show');
  setTimeout(() => elements.toast.classList.remove('show'), duration);
};

const toggleModal = (open = true) => {
  if (open) {
    elements.authModal.classList.add('open');
    elements.authModal.setAttribute('aria-hidden', 'false');
  } else {
    elements.authModal.classList.remove('open');
    elements.authModal.setAttribute('aria-hidden', 'true');
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

const renderHeroStats = services => {
  const stats = [
    { label: 'خدمات متاحة إلكترونياً', value: services.filter(s => s.online).length },
    { label: 'تصنيفات الخدمة', value: new Set(services.map(s => s.category)).size },
    { label: 'زمن الاستجابة المتوسط', value: '48 ساعة' },
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
};

const renderServices = services => {
  const filtered = state.selectedCategory === 'الكل' ? services : services.filter(service => service.category === state.selectedCategory);

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
    elements.servicesList.innerHTML = '<p class="empty-state">لا توجد خدمات ضمن هذا التصنيف حالياً.</p>';
  }
};

const renderServiceDetails = async serviceId => {
  if (!serviceId) {
    elements.serviceDetails.innerHTML = `
      <div class="empty-state">
        <h4>اختر خدمة للاطلاع على تفاصيلها</h4>
        <p>اضغط على أي خدمة من القائمة لاستعراض الوصف، مسار العمل، قنوات التواصل، وإمكانية التقديم الإلكتروني.</p>
      </div>
    `;
    return;
  }

  try {
    const service = await MockApi.getServiceById(serviceId);
    state.selectedService = serviceId;

    const workflowList = service.workflow.map(step => `<li>${step}</li>`).join('');

    elements.serviceDetails.innerHTML = `
      <div class="detail-header">
        <h4>${service.title}</h4>
        <span class="tag">${service.category}</span>
      </div>
      <p>${service.description}</p>
      <h5>مسار العمل</h5>
      <ul>${workflowList}</ul>
      <h5>قنوات التواصل</h5>
      <ul>
        <li>📞 ${service.contact.phone}</li>
        <li>✉️ ${service.contact.email}</li>
        <li>📍 ${service.contact.address}</li>
      </ul>
      ${
        service.online
          ? '<button class="btn" id="applyService">التقديم على الخدمة إلكترونياً</button>'
          : '<p class="empty-state">هذه الخدمة تتطلب الحضور الشخصي حالياً.</p>'
      }
    `;

    elements.serviceDetails.querySelector('#applyService')?.addEventListener('click', handleApplyService);
  } catch (error) {
    showToast(error.message);
  }
};

const statusLabels = {
  pending: 'بانتظار المراجعة',
  'in-progress': 'قيد المعالجة',
  completed: 'منجزة',
  rejected: 'مرفوضة',
};

const renderComplaints = (items = []) => {
  elements.complaintsPanel.innerHTML = `
    <h4>سجل الشكاوى</h4>
    <form id="complaintForm" class="form-grid">
      <label>
        <span>موضوع الشكوى</span>
        <input type="text" name="subject" required />
      </label>
      <label>
        <span>التصنيف</span>
        <input type="text" name="category" placeholder="مثال: مياه، كهرباء" required />
      </label>
      <label>
        <span>تفاصيل الشكوى</span>
        <textarea name="details" required></textarea>
      </label>
      <button class="btn" type="submit">إرسال الشكوى</button>
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
              <th>تحديثات</th>
            </tr>
          </thead>
          <tbody>
            ${items
              .map(
                item => `
                  <tr>
                    <td>${item.id}</td>
                    <td>${item.subject}</td>
                    <td>${item.category}</td>
                    <td><span class="status-tag ${item.status}">${statusLabels[item.status]}</span></td>
                    <td>${item.updates.map(update => `<div>${update.date} - ${update.note}</div>`).join('')}</td>
                  </tr>
                `,
              )
              .join('')}
          </tbody>
        </table>
      `
        : '<p class="empty-state">لم تقم بتسجيل أي شكوى بعد.</p>'
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
      showToast('تم تسجيل الشكوى بنجاح.');
      loadComplaints();
      event.target.reset();
    } catch (error) {
      showToast(error.message);
    }
  });
};

const renderRequests = (items = []) => {
  elements.requestsPanel.innerHTML = `
    <h4>متابعة الطلبات</h4>
    <form id="requestForm" class="form-grid">
      <label>
        <span>موضوع الطلب</span>
        <input type="text" name="subject" required />
      </label>
      <label>
        <span>الجهة المعنية</span>
        <input type="text" name="department" required />
      </label>
      <label>
        <span>تفاصيل الطلب</span>
        <textarea name="details" required></textarea>
      </label>
      <button class="btn" type="submit">إرسال الطلب</button>
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
                    <td>${item.id}</td>
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
        : '<p class="empty-state">لم يتم إرسال أي طلب حتى الآن.</p>'
    }
  `;

  elements.requestsPanel.classList.add('active');
  elements.requestsPanel.querySelector('#requestForm').addEventListener('submit', async event => {
    event.preventDefault();
    if (!state.currentUser) return requireAuth();

    const payload = Object.fromEntries(new FormData(event.target).entries());

    try {
      await MockApi.submitRequest(state.currentUser.id, payload);
      showToast('تم إرسال الطلب بنجاح.');
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
    <h4>خدمة الاقتراحات</h4>
    <form id="suggestionForm" class="form-grid">
      <label>
        <span>عنوان الاقتراح</span>
        <input type="text" name="title" required />
      </label>
      <label>
        <span>وصف مختصر</span>
        <textarea name="body" required></textarea>
      </label>
      <button class="btn" type="submit">إرسال الاقتراح</button>
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
                    <td>${item.title}</td>
                    <td>${item.body}</td>
                    <td>${item.createdAt}</td>
                  </tr>
                `,
              )
              .join('')}
          </tbody>
        </table>
      `
        : '<p class="empty-state">لا توجد اقتراحات بعد، كن أول من يشارك رأيه.</p>'
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
    <h4>المخالفات المسجلة</h4>
    <p>يمكنك استعراض المخالفات والدفع الإلكتروني عند توفره.</p>
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
              <th>تاريخ المخالفة</th>
            </tr>
          </thead>
          <tbody>
            ${items
              .map(
                item => `
                  <tr>
                    <td>${item.id}</td>
                    <td>${item.type}</td>
                    <td>${item.description}</td>
                    <td>${item.amount.toLocaleString('ar-SY')}</td>
                    <td>${item.status}</td>
                    <td>${item.issuedAt}</td>
                  </tr>
                `,
              )
              .join('')}
          </tbody>
        </table>
        <div class="detail-header" style="margin-top:1.5rem;">
          <span class="tag">إجمالي الذمم</span>
          <strong>${total.toLocaleString('ar-SY')} ل.س</strong>
        </div>
      `
        : '<p class="empty-state">لا توجد مخالفات مسجلة.</p>'
    }
  `;

  elements.violationsPanel.classList.add('active');
};

const requireAuth = () => {
  showToast('الرجاء تسجيل الدخول لإتمام هذه العملية.');
  toggleModal(true);
};

const handleApplyService = () => {
  if (!state.currentUser) {
    requireAuth();
    return;
  }
  showToast('تم تهيئة طلبك للخدمة، سيجري تحويلك لواجهة التقديم قريباً.');
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
  elements.authModal.addEventListener('click', event => {
    if (event.target.dataset.close !== undefined || event.target === elements.authModal) {
      toggleModal(false);
    }
  });

  elements.authModal.querySelectorAll('.modal__tab').forEach(tab => {
    tab.addEventListener('click', () => switchAuthTab(tab.dataset.tab));
  });

  elements.loginForm.addEventListener('submit', async event => {
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

  elements.registerForm.addEventListener('submit', async event => {
    event.preventDefault();
    const payload = Object.fromEntries(new FormData(event.target).entries());
    try {
      const { phone, otp } = await MockApi.register(payload);
      state.otpPhone = phone;
      switchAuthTab('otp');
      const hint = elements.otpForm.querySelector('.otp__hint');
      hint.textContent = `تم إرسال كلمة مرور لمرة واحدة إلى الرقم ${phone}. (رمز تجريبي: ${otp})`;
    } catch (error) {
      showToast(error.message);
    }
  });

  elements.otpForm.addEventListener('submit', async event => {
    event.preventDefault();
    const { otp } = Object.fromEntries(new FormData(event.target).entries());
    if (!state.otpPhone) return;

    try {
      const user = await MockApi.verifyOtp(state.otpPhone, otp);
      state.currentUser = user;
      state.otpPhone = null;
      switchAuthTab('login');
      toggleModal(false);
      showToast('تم تفعيل الحساب بنجاح! يمكنك الآن استخدام المنصة.');
      loadComplaints();
      loadRequests();
      updateUserActions();
    } catch (error) {
      showToast(error.message);
    }
  });
};

const initNavigationCards = () => {
  document.querySelectorAll('.card').forEach(card => {
    card.addEventListener('click', () => {
      const target = card.dataset.target;
      const panel = document.getElementById(`${target}Panel`);
      if (!panel) return;
      panel.scrollIntoView({ behavior: 'smooth', block: 'start' });
      panel.classList.add('active');
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

    elements.categoryFilters.addEventListener('click', event => {
      if (event.target.matches('.filter')) {
        state.selectedCategory = event.target.dataset.category;
        renderFilters(state.services);
        renderServices(state.services);
        renderServiceDetails(null);
      }
    });

    elements.servicesList.addEventListener('click', event => {
      const card = event.target.closest('[data-service-id]');
      if (!card) return;
      const serviceId = card.dataset.serviceId;
      renderServiceDetails(serviceId);
      renderServices(state.services);
    });
  } catch (error) {
    showToast('تعذر تحميل الخدمات حالياً.');
    console.error(error);
  }
};

const initCTAButtons = () => {
  document.getElementById('exploreServices').addEventListener('click', () => {
    document.getElementById('servicesSection').scrollIntoView({ behavior: 'smooth', block: 'start' });
  });

  document.getElementById('contactSupport').addEventListener('click', () => {
    window.location.href = 'tel:015123456';
  });
};

const init = async () => {
  updateUserActions();
  initAuth();
  initNavigationCards();
  initCTAButtons();
  await initServices();
  await initPanels();
};

window.addEventListener('DOMContentLoaded', init);
