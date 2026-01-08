const MockApi = (() => {
  const users = [
    {
      id: 'user-001',
      fullName: 'أحمد محمود النابلسي',
      nationalId: '01020304050',
      phone: '0955123456',
      password: 'Pass@123',
      otp: null,
      verified: true,
    },
  ];

  const services = [
    {
      id: 'consular-legalization',
      title: 'خدمة تصديق الوثائق',
      category: 'خدمات قنصلية',
      description: 'تصديق الوثائق الرسمية الصادرة عن الجهات الحكومية أو الجهات الخاصة المعتمدة خارج القطر.',
      contact: {
        email: 'consular@daraa.gov.sy',
        phone: '015600100',
        address: 'مديرية الشؤون القنصلية - الطابق الثاني',
      },
      workflow: ['تقديم الطلب إلكترونياً أو عبر المركز.', 'تدقيق الوثائق من قبل الموظف المختص.', 'إصدار التصديق خلال 48 ساعة.'],
      tags: ['مستوى خدمة عالي', 'متاحة إلكترونياً'],
      online: true,
    },
    {
      id: 'civil-registry-family',
      title: 'خدمة بيان عائلي',
      category: 'خدمات سجل النفوس',
      description: 'استخراج بيان عائلي محدث للأسر المسجلة في محافظة درعا.',
      contact: {
        email: 'civil@daraa.gov.sy',
        phone: '015600120',
        address: 'مديرية الأحوال المدنية - مركز خدمة المواطن',
      },
      workflow: [
        'تقديم الطلب عبر البوابة أو النافذة الواحدة.',
        'مطابقة البيانات مع السجل المدني.',
        'إرسال النسخة الرقمية إلى البريد المسجل.',
      ],
      tags: ['مستندات شخصية', 'يتطلب تسجيل دخول'],
      online: true,
    },
    {
      id: 'water-subscription',
      title: 'نقل اشتراك مياه',
      category: 'خدمات مؤسسة المياه',
      description: 'تقديم طلب نقل اشتراك مياه من مشترك إلى آخر داخل محافظة درعا.',
      contact: {
        email: 'water@daraa.gov.sy',
        phone: '015600220',
        address: 'مؤسسة مياه درعا - دائرة الاشتراكات',
      },
      workflow: ['تعبئة نموذج الطلب.', 'زيارة ميدانية للتأكد من حالة العداد.', 'تحديث بيانات الاشتراك ودفع الرسوم المستحقة.'],
      tags: ['يتطلب معاينة'],
      online: false,
    },
    {
      id: 'realestate-statement',
      title: 'بيان ملكية عقارية',
      category: 'خدمات المصالح العقارية',
      description: 'الحصول على بيان ملكية للعقارات المسجلة في مديرية المصالح العقارية بدرعا.',
      contact: {
        email: 'estate@daraa.gov.sy',
        phone: '015600320',
        address: 'مديرية المصالح العقارية - مكتب السجلات',
      },
      workflow: ['رفع طلب مع الوثائق المرفقة.', 'تدقيق الملكية في السجلات.', 'استلام البيان خلال 3 أيام عمل.'],
      tags: ['مدفوع الرسوم'],
      online: true,
    },
    {
      id: 'education-certificates',
      title: 'تصديق الشهادات الدراسية',
      category: 'خدمات مديرية التربية',
      description: 'خدمة تصديق الشهادات الثانوية والتعليم الأساسي الصادرة عن مديرية التربية.',
      contact: {
        email: 'education@daraa.gov.sy',
        phone: '015600420',
        address: 'مديرية التربية - دائرة الامتحانات',
      },
      workflow: ['تقديم الطلب عبر المنصة.', 'مطابقة بيانات الطالب.', 'إصدار التصديق وتسليمه إلكترونياً.'],
      tags: ['يدعم الدفع الإلكتروني'],
      online: true,
    },
    {
      id: 'social-support',
      title: 'طلب مساعدة اجتماعية عاجلة',
      category: 'خدمات الشؤون الاجتماعية والعمل',
      description: 'تقديم طلب للحصول على مساعدة اجتماعية عاجلة للأسر المتضررة.',
      contact: {
        email: 'social@daraa.gov.sy',
        phone: '015600520',
        address: 'مديرية الشؤون الاجتماعية والعمل - مكتب الإغاثة',
      },
      workflow: ['تقديم الطلب وإرفاق الوثائق.', 'زيارة ميدانية لتقييم الحالة.', 'إصدار قرار المنحة وتحويل المبلغ.'],
      tags: ['أولوية قصوى'],
      online: true,
    },
  ];

  const complaints = [
    {
      id: 'cmp-1001',
      subject: 'انقطاع مياه الشرب في حي السد',
      category: 'مياه الشرب',
      status: 'in-progress',
      createdAt: '2024-05-12',
      updates: [
        { date: '2024-05-13', note: 'تم تحويل الشكوى إلى مؤسسة المياه.' },
        { date: '2024-05-15', note: 'تجري صيانة عاجلة للخط الرئيسي.' },
      ],
      userId: 'user-001',
    },
    {
      id: 'cmp-1002',
      subject: 'تأخر إنجاز معاملة بيان قيد مدني',
      category: 'الأحوال المدنية',
      status: 'pending',
      createdAt: '2024-05-18',
      updates: [{ date: '2024-05-19', note: 'تم تعيين موظف للمتابعة.' }],
      userId: 'user-001',
    },
  ];

  const requests = [
    {
      id: 'req-2001',
      subject: 'طلب مساعدة مالية لعائلة متضررة',
      department: 'الشؤون الاجتماعية والعمل',
      status: 'completed',
      createdAt: '2024-04-02',
      userId: 'user-001',
    },
    {
      id: 'req-2002',
      subject: 'طلب تصريح بناء طابق إضافي',
      department: 'الوحدة الإدارية بدرعا',
      status: 'in-progress',
      createdAt: '2024-05-21',
      userId: 'user-001',
    },
  ];

  const suggestions = [
    {
      id: 'sug-3001',
      title: 'إتاحة مواعيد إلكترونية لمركز خدمة المواطن',
      body: 'توفير نظام حجز مواعيد إلكترونية يقلل الازدحام ويسرع تقديم الخدمات.',
      createdAt: '2024-03-01',
    },
  ];

  const violations = [
    {
      id: 'vio-4001',
      type: 'مخالفة مرورية',
      description: 'تجاوز السرعة المحددة ضمن المدينة.',
      amount: 45000,
      status: 'غير مسددة',
      issuedAt: '2024-05-08',
    },
    {
      id: 'vio-4002',
      type: 'مخالفة نظافة',
      description: 'رمي مخلفات البناء دون تصريح.',
      amount: 60000,
      status: 'قيد المتابعة',
      issuedAt: '2024-04-28',
    },
  ];

  const delay = (min = 300, max = 650) => new Promise(resolve => setTimeout(resolve, Math.random() * (max - min) + min));

  const response = async (data, error = null) => {
    await delay();
    if (error) throw error;
    return JSON.parse(JSON.stringify(data));
  };

  return {
    async login(phone, password) {
      const user = users.find(u => u.phone === phone && u.password === password && u.verified);
      if (!user) {
        return response(null, new Error('بيانات الدخول غير صحيحة أو الحساب غير مفعل.'));
      }
      return response({ ...user, password: undefined, otp: undefined });
    },

    async register(payload) {
      const exists = users.some(u => u.phone === payload.phone || u.nationalId === payload.nationalId);
      if (exists) {
        return response(null, new Error('يوجد حساب مسجل بنفس البيانات.'));
      }
      const otpCode = String(Math.floor(100000 + Math.random() * 900000));
      const newUser = {
        id: `user-${String(users.length + 1).padStart(3, '0')}`,
        ...payload,
        otp: otpCode,
        verified: false,
      };
      users.push(newUser);
      return response({ phone: newUser.phone, otp: otpCode });
    },

    async verifyOtp(phone, otp) {
      const user = users.find(u => u.phone === phone);
      if (!user) return response(null, new Error('المستخدم غير موجود.'));
      if (user.otp !== otp) return response(null, new Error('رمز التفعيل غير صحيح.'));
      user.verified = true;
      user.otp = null;
      return response({ ...user, password: undefined });
    },

    async getServices() {
      return response(services);
    },

    async getServiceById(id) {
      const service = services.find(s => s.id === id);
      if (!service) return response(null, new Error('الخدمة غير موجودة.'));
      return response(service);
    },

    async submitComplaint(userId, payload) {
      const newComplaint = {
        id: `cmp-${Math.floor(Math.random() * 9000 + 1000)}`,
        status: 'pending',
        createdAt: new Date().toISOString().split('T')[0],
        updates: [{ date: new Date().toISOString().split('T')[0], note: 'تم استلام الشكوى وجاري تحويلها.' }],
        userId,
        ...payload,
      };
      complaints.unshift(newComplaint);
      return response(newComplaint);
    },

    async getComplaints(userId) {
      return response(complaints.filter(c => c.userId === userId));
    },

    async submitRequest(userId, payload) {
      const newRequest = {
        id: `req-${Math.floor(Math.random() * 9000 + 1000)}`,
        status: 'pending',
        createdAt: new Date().toISOString().split('T')[0],
        userId,
        ...payload,
      };
      requests.unshift(newRequest);
      return response(newRequest);
    },

    async getRequests(userId) {
      return response(requests.filter(r => r.userId === userId));
    },

    async submitSuggestion(payload) {
      const suggestion = {
        id: `sug-${Math.floor(Math.random() * 9000 + 1000)}`,
        createdAt: new Date().toISOString().split('T')[0],
        ...payload,
      };
      suggestions.unshift(suggestion);
      return response(suggestion);
    },

    async getSuggestions() {
      return response(suggestions);
    },

    async getViolations() {
      return response(violations);
    },
  };
})();

window.MockApi = MockApi;
