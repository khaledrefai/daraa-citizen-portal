import { ICitizenService } from 'app/shared/model/citizen-service.model';
import { IDirectorate } from 'app/shared/model/directorate.model';
import { IRequiredDocument } from 'app/shared/model/required-document.model';
import { IServiceCategory } from 'app/shared/model/service-category.model';
import { IServiceFormTemplate } from 'app/shared/model/service-form-template.model';
import { EstimatedTimeUnit } from 'app/shared/model/enumerations/estimated-time-unit.model';

export interface AssistantSource {
  serviceId: number;
  name: string;
  description?: string;
  score?: number;
}

export interface AssistantChatResponse {
  answer: string;
  sources: AssistantSource[];
}

const wait = (ms = 250) => new Promise(resolve => setTimeout(resolve, ms));

const directorates: IDirectorate[] = [
  {
    id: 1,
    code: 'CIVIL',
    name: 'مديرية الشؤون المدنية',
    description: 'خدمات الأحوال المدنية وإصدار الوثائق الرسمية.',
    active: true,
  },
  {
    id: 2,
    code: 'TRANSPORT',
    name: 'مديرية النقل',
    description: 'خدمات المركبات والرخص وتجديد الوثائق المرورية.',
    active: true,
  },
  {
    id: 3,
    code: 'SOCIAL',
    name: 'مديرية الشؤون الاجتماعية والعمل',
    description: 'برامج الرعاية والدعم الاجتماعي وخدمات العمل.',
    active: true,
  },
];

const serviceCategories: IServiceCategory[] = [
  {
    id: 11,
    code: 'ID_DOCS',
    name: 'وثائق الهوية',
    description: 'إصدار وتجديد بطاقات الهوية الشخصية.',
    active: true,
    directorate: directorates[0],
  },
  {
    id: 12,
    code: 'BIRTH_MARRIAGE',
    name: 'الواقعات المدنية',
    description: 'تسجيل واقعات الولادة والزواج والطلاق.',
    active: true,
    directorate: directorates[0],
  },
  {
    id: 21,
    code: 'VEHICLE',
    name: 'خدمات المركبات',
    description: 'ترخيص المركبات وتجديدها ونقل الملكية.',
    active: true,
    directorate: directorates[1],
  },
  {
    id: 31,
    code: 'AID',
    name: 'الدعم الاجتماعي',
    description: 'الطلبات المتعلقة بالمساعدات والخدمات الاجتماعية.',
    active: true,
    directorate: directorates[2],
  },
];

const citizenServices: ICitizenService[] = [
  {
    id: 101,
    name: 'تجديد البطاقة الشخصية',
    description: 'تقديم طلب تجديد البطاقة الشخصية للمواطن عند انتهاء الصلاحية أو التلف.',
    estimatedDuration: 3,
    estimatedDurationUnit: EstimatedTimeUnit.DAY,
    requiresPhysicalPresence: true,
    isElectronic: true,
    hasSmartCard: true,
    feesDescription: '10,000 ل.س رسوم إصدار + طابع محلي.',
    serviceLink: 'https://services.daraa.gov.sy/id/renew',
    active: true,
    category: serviceCategories[0],
  },
  {
    id: 102,
    name: 'إصدار بيان ولادة',
    description: 'استخراج بيان ولادة رسمي مصدق للاستخدامات المحلية والخارجية.',
    estimatedDuration: 30,
    estimatedDurationUnit: EstimatedTimeUnit.MINUTE,
    requiresPhysicalPresence: false,
    isElectronic: true,
    hasSmartCard: false,
    feesDescription: '2,000 ل.س.',
    serviceLink: 'https://services.daraa.gov.sy/civil/birth-certificate',
    active: true,
    category: serviceCategories[1],
  },
  {
    id: 201,
    name: 'تجديد رخصة سير مركبة',
    description: 'تجديد رخصة السير السنوية للمركبة بعد استكمال الفحص الفني.',
    estimatedDuration: 2,
    estimatedDurationUnit: EstimatedTimeUnit.HOUR,
    requiresPhysicalPresence: true,
    isElectronic: false,
    hasSmartCard: false,
    feesDescription: 'بحسب فئة المركبة.',
    serviceLink: null,
    active: true,
    category: serviceCategories[2],
  },
  {
    id: 301,
    name: 'طلب مساعدة اجتماعية طارئة',
    description: 'تقديم طلب مساعدة للحالات الاجتماعية الأكثر احتياجاً مع دراسة ميدانية.',
    estimatedDuration: 1,
    estimatedDurationUnit: EstimatedTimeUnit.WEEK,
    requiresPhysicalPresence: false,
    isElectronic: true,
    hasSmartCard: false,
    feesDescription: 'بدون رسوم.',
    serviceLink: 'https://services.daraa.gov.sy/social/emergency-aid',
    active: true,
    category: serviceCategories[3],
  },
  {
    id: 302,
    name: 'تحديث بيانات المستفيد',
    description: 'تحديث بيانات الأسر المستفيدة من برامج الدعم الاجتماعي.',
    estimatedDuration: 1,
    estimatedDurationUnit: EstimatedTimeUnit.DAY,
    requiresPhysicalPresence: false,
    isElectronic: true,
    hasSmartCard: false,
    feesDescription: 'بدون رسوم.',
    serviceLink: 'https://services.daraa.gov.sy/social/update-profile',
    active: true,
    category: serviceCategories[3],
  },
];

const requiredDocumentsByServiceId: Record<number, IRequiredDocument[]> = {
  101: [
    { id: 1001, name: 'البطاقة القديمة', description: 'نسخة أصلية أو بدل ضائع مع ضبط شرطة.', mandatory: true, orderIndex: 1 },
    { id: 1002, name: 'صور شخصية', description: 'عدد 2 بخلفية بيضاء.', mandatory: true, orderIndex: 2 },
    { id: 1003, name: 'إيصال الرسوم', description: 'إيصال تسديد الرسوم المطلوبة.', mandatory: true, orderIndex: 3 },
  ],
  102: [
    { id: 1101, name: 'رقم القيد المدني', description: 'الرقم الوطني أو بيانات القيد.', mandatory: true, orderIndex: 1 },
    { id: 1102, name: 'تفويض قانوني', description: 'في حال الطلب من وكيل.', mandatory: false, orderIndex: 2 },
  ],
  201: [
    { id: 1201, name: 'رخصة السير السابقة', description: 'نسخة أصلية سارية أو منتهية حديثاً.', mandatory: true, orderIndex: 1 },
    { id: 1202, name: 'تقرير الفحص الفني', description: 'تقرير معتمد من مركز الفحص.', mandatory: true, orderIndex: 2 },
    { id: 1203, name: 'بوليصة التأمين', description: 'وثيقة تأمين سارية للمركبة.', mandatory: true, orderIndex: 3 },
  ],
  301: [
    { id: 1301, name: 'بيان عائلي', description: 'لإثبات الحالة الأسرية.', mandatory: true, orderIndex: 1 },
    { id: 1302, name: 'تقرير دخل', description: 'أو ما يثبت عدم وجود دخل ثابت.', mandatory: true, orderIndex: 2 },
  ],
  302: [{ id: 1401, name: 'هوية رب الأسرة', description: 'صورة عن البطاقة الشخصية.', mandatory: true, orderIndex: 1 }],
};

const samplePdfBase64 =
  'JVBERi0xLjQKJcTl8uXrp/Og0MTGCjEgMCBvYmoKPDwvVHlwZSAvQ2F0YWxvZz4+CmVuZG9iago=';

const formTemplatesByServiceId: Record<number, IServiceFormTemplate[]> = {
  101: [
    {
      id: 2001,
      name: 'نموذج طلب تجديد بطاقة شخصية.pdf',
      description: 'يعبأ النموذج ويوقع قبل المراجعة.',
      fileContentType: 'application/pdf',
      file: samplePdfBase64,
      active: true,
      orderIndex: 1,
    },
  ],
  301: [
    {
      id: 2301,
      name: 'استمارة دراسة حالة اجتماعية.pdf',
      description: 'يعبأ من قبل مقدم الطلب.',
      fileContentType: 'application/pdf',
      file: samplePdfBase64,
      active: true,
      orderIndex: 1,
    },
  ],
  302: [
    {
      id: 2401,
      name: 'نموذج تحديث بيانات المستفيد.pdf',
      description: 'مخصص لتعديل المعلومات الأساسية.',
      fileContentType: 'application/pdf',
      file: samplePdfBase64,
      active: true,
      orderIndex: 1,
    },
  ],
};

const normalize = (value?: string | null) => (value ?? '').toLowerCase();

const sortByOrderIndex = <T extends { orderIndex?: number | null }>(items: T[]) =>
  [...items].sort((a, b) => (a.orderIndex ?? 0) - (b.orderIndex ?? 0));

export const mockCitizenPortalApi = {
  async getCitizenServices(): Promise<ICitizenService[]> {
    await wait();
    return [...citizenServices].sort((a, b) => normalize(a.name).localeCompare(normalize(b.name), 'ar'));
  },

  async getCitizenService(id: string | number): Promise<ICitizenService> {
    await wait();
    const numericId = Number(id);
    const service = citizenServices.find(item => item.id === numericId);
    if (!service) {
      throw new Error('Service not found');
    }
    return service;
  },

  async getServiceCategories(): Promise<IServiceCategory[]> {
    await wait();
    return [...serviceCategories].sort((a, b) => normalize(a.name).localeCompare(normalize(b.name), 'ar'));
  },

  async getDirectorates(): Promise<IDirectorate[]> {
    await wait();
    return [...directorates].sort((a, b) => normalize(a.name).localeCompare(normalize(b.name), 'ar'));
  },

  async getRequiredDocumentsByServiceId(serviceId: string | number): Promise<IRequiredDocument[]> {
    await wait();
    const numericId = Number(serviceId);
    const docs = requiredDocumentsByServiceId[numericId] ?? [];
    return sortByOrderIndex(docs).map(doc => ({
      ...doc,
      service: citizenServices.find(item => item.id === numericId) ?? null,
    }));
  },

  async getServiceFormTemplatesByServiceId(serviceId: string | number): Promise<IServiceFormTemplate[]> {
    await wait();
    const numericId = Number(serviceId);
    const templates = formTemplatesByServiceId[numericId] ?? [];
    return sortByOrderIndex(templates).map(template => ({
      ...template,
      service: citizenServices.find(item => item.id === numericId) ?? null,
    }));
  },

  async chat(question: string): Promise<AssistantChatResponse> {
    await wait(400);
    const normalizedQuestion = normalize(question);

    const matches = citizenServices
      .map(service => {
        const haystack = [service.name, service.description, service.category?.name, service.category?.directorate?.name]
          .map(normalize)
          .join(' ');

        let score = 0;
        normalizedQuestion
          .split(/\s+/)
          .filter(Boolean)
          .forEach(token => {
            if (haystack.includes(token)) {
              score += 1;
            }
          });

        return { service, score };
      })
      .filter(item => item.score > 0)
      .sort((a, b) => b.score - a.score)
      .slice(0, 3);

    if (matches.length === 0) {
      return {
        answer:
          'لم أجد خدمة مطابقة بشكل مباشر. حاول ذكر اسم الخدمة أو الجهة (مثل: الشؤون المدنية، النقل، الدعم الاجتماعي) للحصول على نتيجة أدق.',
        sources: [],
      };
    }

    const sources: AssistantSource[] = matches.map(match => ({
      serviceId: match.service.id ?? 0,
      name: match.service.name ?? '',
      description: match.service.description,
      score: match.score,
    }));

    const topService = matches[0].service;
    const duration = topService.estimatedDuration && topService.estimatedDurationUnit
      ? `${topService.estimatedDuration} ${topService.estimatedDurationUnit}`
      : 'غير محدد';

    return {
      answer: `أنسب خدمة لسؤالك هي: ${topService.name}. الزمن المتوقع: ${duration}. ${
        topService.isElectronic ? 'الخدمة متاحة إلكترونياً.' : 'الخدمة تتطلب مراجعة الجهة المختصة.'
      }`,
      sources,
    };
  },
};
