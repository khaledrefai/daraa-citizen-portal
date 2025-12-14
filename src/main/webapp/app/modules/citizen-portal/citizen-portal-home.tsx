import './citizen-portal.scss';

import React, { useEffect, useMemo, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { Alert, Badge, Button, Collapse, Input, InputGroup, InputGroupText, Label, Spinner } from 'reactstrap';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ICitizenService } from 'app/shared/model/citizen-service.model';
import { IServiceCategory } from 'app/shared/model/service-category.model';
import { IDirectorate } from 'app/shared/model/directorate.model';
import CitizenAssistant from './citizen-assistant';

const durationLabels: Record<string, string> = {
  MINUTE: 'دقيقة',
  HOUR: 'ساعة',
  DAY: 'يوم',
  WEEK: 'أسبوع',
};

type MenuMode = 'directorates' | 'categories';

const CitizenPortalHome = () => {
  const [services, setServices] = useState<ICitizenService[]>([]);
  const [categories, setCategories] = useState<IServiceCategory[]>([]);
  const [directorates, setDirectorates] = useState<IDirectorate[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedDirectorate, setSelectedDirectorate] = useState<string>('all');
  const [selectedCategory, setSelectedCategory] = useState<string>('all');
  const [onlyElectronic, setOnlyElectronic] = useState(false);
  const [menuMode, setMenuMode] = useState<MenuMode>('directorates');
  const [filtersOpen, setFiltersOpen] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setError(null);
      try {
        const [servicesResponse, categoriesResponse, directoratesResponse] = await Promise.all([
          axios.get<ICitizenService[]>('api/citizen-services?active.equals=true&sort=name,asc'),
          axios.get<IServiceCategory[]>('api/service-categories?active.equals=true&sort=name,asc'),
          axios.get<IDirectorate[]>('api/directorates?active.equals=true&sort=name,asc'),
        ]);
        setServices(servicesResponse.data);
        setCategories(categoriesResponse.data);
        setDirectorates(directoratesResponse.data);
      } catch (err) {
        setError('citizenPortal.errors.load');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleDirectorateSelect = (value: string) => {
    setSelectedDirectorate(value);
    setSelectedCategory('all');
    setMenuMode('directorates');
  };

  const handleCategorySelect = (value: string) => {
    if (value === 'all') {
      setSelectedCategory('all');
      setSelectedDirectorate('all');
      setMenuMode('categories');
      return;
    }

    setSelectedCategory(value);
    setMenuMode('categories');
    const category = categories.find(item => item.id === Number(value));
    if (category?.directorate?.id) {
      setSelectedDirectorate(category.directorate.id.toString());
    }
  };

  const filteredCategories = useMemo(
    () => categories.filter(category => (selectedDirectorate === 'all' ? true : category.directorate?.id === Number(selectedDirectorate))),
    [categories, selectedDirectorate],
  );

  const filteredServices = useMemo(() => {
    const term = searchTerm.trim().toLowerCase();
    return services
      .filter(service => service.active)
      .filter(service => (selectedDirectorate === 'all' ? true : service.category?.directorate?.id === Number(selectedDirectorate)))
      .filter(service => (selectedCategory === 'all' ? true : service.category?.id === Number(selectedCategory)))
      .filter(service => (onlyElectronic ? service.isElectronic : true))
      .filter(service =>
        !term
          ? true
          : [service.name, service.description, service.category?.name, service.category?.directorate?.name, service.code].some(value =>
              value?.toLowerCase().includes(term),
            ),
      );
  }, [services, selectedDirectorate, selectedCategory, onlyElectronic, searchTerm]);

  const formatDuration = (service: ICitizenService) => {
    if (!service.estimatedDuration || !service.estimatedDurationUnit) {
      return 'غير محددة';
    }
    return `${service.estimatedDuration} ${durationLabels[service.estimatedDurationUnit] ?? translate('citizenPortal.timeUnits.minute')}`;
  };

  const menuItems: Array<{ id?: string; label: string; count?: number }> =
    menuMode === 'directorates'
      ? [
          { id: 'all', label: 'كل الجهات' },
          ...directorates.map(directorate => ({
            id: directorate.id?.toString(),
            label: directorate.name,
            count: services.filter(service => service.category?.directorate?.id === directorate.id).length,
          })),
        ]
      : [
          { id: 'all', label: 'كل التصنيفات' },
          ...filteredCategories.map(category => ({
            id: category.id?.toString(),
            label: category.name,
            count: services.filter(service => service.category?.id === category.id).length,
          })),
        ];

  return (
    <div className="citizen-page">
      <div className="citizen-hero">
        <div className="hero-text">
          <p className="hero-kicker">دليل خدمات المواطن</p>
          <h1>اختَر من القائمة الجانبية لتظهر لك الخدمات كبطاقات منظمة</h1>
          <p>واجهة عربية بالكامل من اليمين إلى اليسار مع استخدام خط قمرة والهوية الموحدة للمحافظة.</p>
          <div className="hero-stats">
            <div className="hero-stat">
              <p>الجهات</p>
              <strong>{directorates.length}</strong>
            </div>
            <div className="hero-stat">
              <p>تصانيف الخدمات</p>
              <strong>{categories.length}</strong>
            </div>
            <div className="hero-stat">
              <p>الخدمات المتاحة</p>
              <strong>{services.length}</strong>
            </div>
          </div>
        </div>
        <div className="hero-brand">
          <img src="assets/images/logo.ai.svg" alt="شعار بوابة درعا" />
        </div>
      </div>

      {error && (
        <Alert color="danger" className="mt-3">
          {translate(error)}
        </Alert>
      )}

      <div className="citizen-layout">
        <aside className="filters-card side-filter">
          <div className="filters-card__header">
            <p className="eyebrow">القائمة الجانبية</p>
            <p className="filters-card__subtitle">تصفية حسب الجهة أو تصنيف الخدمة مع خيارات منسدلة</p>
          </div>

          <div className="side-menu-tabs">
            <Button
              color={menuMode === 'directorates' ? 'primary' : 'secondary'}
              outline={menuMode !== 'directorates'}
              className="side-tab"
              onClick={() => setMenuMode('directorates')}
            >
              <FontAwesomeIcon icon="building" className="ms-1" />
              <span>الجهات</span>
            </Button>
            <Button
              color={menuMode === 'categories' ? 'primary' : 'secondary'}
              outline={menuMode !== 'categories'}
              className="side-tab"
              onClick={() => setMenuMode('categories')}
            >
              <FontAwesomeIcon icon="list" className="ms-1" />
              <span>تصانيف الخدمات</span>
            </Button>
          </div>

          <Collapse isOpen={filtersOpen}>
            <div className="filter-field">
              <Label for="searchTerm">بحث سريع</Label>
              <InputGroup>
                <InputGroupText>
                  <FontAwesomeIcon icon="search" />
                </InputGroupText>
                <Input
                  id="searchTerm"
                  type="search"
                  value={searchTerm}
                  onChange={event => setSearchTerm(event.target.value)}
                  placeholder="اكتب اسم الخدمة أو رمزها"
                />
              </InputGroup>
            </div>

            <div className="side-menu-list">
              <Label className="list-label">{menuMode === 'directorates' ? 'الجهات' : 'التصنيفات'}</Label>
              <div className="list-scroll">
                {menuItems.map(item => (
                  <button
                    type="button"
                    key={item.id ?? 'empty'}
                    className={`side-list-item ${
                      (menuMode === 'directorates' ? selectedDirectorate : selectedCategory) === (item.id ?? '').toString() ? 'active' : ''
                    }`}
                    onClick={() =>
                      menuMode === 'directorates' ? handleDirectorateSelect(item.id ?? 'all') : handleCategorySelect(item.id ?? 'all')
                    }
                  >
                    <span>{item.label}</span>
                    {typeof item.count === 'number' && <span className="item-count">{item.count}</span>}
                  </button>
                ))}
              </div>
            </div>

            <div className="filter-field switch-field">
              <Label className="form-check-label" htmlFor="electronic-only">
                الخدمات الإلكترونية فقط
              </Label>
              <Input
                id="electronic-only"
                type="switch"
                className="form-check-input"
                checked={onlyElectronic}
                onChange={event => setOnlyElectronic(event.target.checked)}
              />
            </div>
          </Collapse>

          <Button color="link" className="toggle-filters" onClick={() => setFiltersOpen(!filtersOpen)}>
            <FontAwesomeIcon icon={filtersOpen ? 'list' : 'plus'} />
            <span className="ms-2">{filtersOpen ? 'إخفاء القائمة الجانبية' : 'إظهار القائمة الجانبية'}</span>
          </Button>
        </aside>

        <section className="services-area">
          {loading ? (
            <div className="loading-state">
              <Spinner color="primary" /> <span className="ms-2">جاري تحميل الخدمات...</span>
            </div>
          ) : filteredServices.length === 0 ? (
            <div className="empty-state">
              <p>لا توجد خدمات مطابقة حالياً.</p>
              <p className="text-muted">جرّب تغيير الجهة أو التصنيف من القائمة الجانبية.</p>
            </div>
          ) : (
            <div className="service-grid">
              {filteredServices.map(service => (
                <div className="service-card" key={service.id}>
                  <div className="service-card__top">
                    <span className="service-code">رمز الخدمة: {service.code}</span>
                    <div className="service-badges">
                      {service.isElectronic && <Badge color="success">إلكترونية</Badge>}
                      {service.requiresPhysicalPresence && <Badge color="warning">تتطلب حضور</Badge>}
                      {service.hasSmartCard && <Badge color="info">تتضمن بطاقة ذكية</Badge>}
                    </div>
                  </div>

                  <h3>{service.name}</h3>
                  <p className="service-description">{service.description}</p>

                  <div className="service-meta-row">
                    <div className="meta-pill">
                      <span className="meta-label">الجهة</span>
                      <strong>{service.category?.directorate?.name ?? 'غير محددة'}</strong>
                    </div>
                    <div className="meta-pill">
                      <span className="meta-label">التصنيف</span>
                      <strong>{service.category?.name ?? 'غير محدد'}</strong>
                    </div>
                    <div className="meta-pill">
                      <span className="meta-label">المدة التقديرية</span>
                      <strong>{formatDuration(service)}</strong>
                    </div>
                  </div>

                  <div className="service-actions">
                    {service.feesDescription && <span className="fees">الرسوم: {service.feesDescription}</span>}
                    <Button tag={Link} to={`/citizen/services/${service.id}`} color="primary" size="sm">
                      عرض التفاصيل
                    </Button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </section>
      </div>
      <CitizenAssistant />
    </div>
  );
};

export default CitizenPortalHome;
