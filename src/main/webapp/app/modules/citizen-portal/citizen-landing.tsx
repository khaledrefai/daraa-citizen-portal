import './citizen-portal.scss';

import React, { useEffect, useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import { Badge, Button, Col, Input, Label, Row, Spinner } from 'reactstrap';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getDirectorates } from 'app/entities/directorate/directorate.reducer';
import { getEntities as getServiceCategories } from 'app/entities/service-category/service-category.reducer';
import { getEntities as getCitizenServices } from 'app/entities/citizen-service/citizen-service.reducer';
import { ICitizenService } from 'app/shared/model/citizen-service.model';
import { EstimatedTimeUnit } from 'app/shared/model/enumerations/estimated-time-unit.model';

const durationLabels: Record<keyof typeof EstimatedTimeUnit, string> = {
  MINUTE: 'دقيقة',
  HOUR: 'ساعة',
  DAY: 'يوم',
  WEEK: 'أسبوع',
};

const formatDuration = (service: ICitizenService) => {
  if (!service.estimatedDuration || !service.estimatedDurationUnit) {
    return 'غير محددة';
  }

  return `${service.estimatedDuration} ${durationLabels[service.estimatedDurationUnit] ?? service.estimatedDurationUnit.toLowerCase()}`;
};

const CitizenLanding = () => {
  const dispatch = useAppDispatch();
  const [search, setSearch] = useState('');
  const [selectedDirectorate, setSelectedDirectorate] = useState<string>('all');
  const [selectedCategory, setSelectedCategory] = useState<string>('all');
  const [onlyElectronic, setOnlyElectronic] = useState(false);

  const directorates = useAppSelector(state => state.directorate.entities);
  const directorateLoading = useAppSelector(state => state.directorate.loading);
  const categories = useAppSelector(state => state.serviceCategory.entities);
  const categoryLoading = useAppSelector(state => state.serviceCategory.loading);
  const services = useAppSelector(state => state.citizenService.entities);
  const serviceLoading = useAppSelector(state => state.citizenService.loading);

  useEffect(() => {
    dispatch(getDirectorates({ page: 0, size: 1000, sort: 'name,asc' }));
    dispatch(getServiceCategories({ page: 0, size: 1000, sort: 'name,asc' }));
    dispatch(getCitizenServices({ page: 0, size: 1000, sort: 'name,asc' }));
  }, [dispatch]);

  useEffect(() => {
    if (selectedCategory !== 'all') {
      const validCategory = categories.find(
        category =>
          category.id === Number(selectedCategory) &&
          (selectedDirectorate === 'all' || category.directorate?.id === Number(selectedDirectorate)),
      );
      if (!validCategory) {
        setSelectedCategory('all');
      }
    }
  }, [categories, selectedCategory, selectedDirectorate]);

  const filteredCategories = useMemo(
    () => categories.filter(category => selectedDirectorate === 'all' || category.directorate?.id === Number(selectedDirectorate)),
    [categories, selectedDirectorate],
  );

  const filteredServices = useMemo(
    () =>
      services
        .filter(service => service.active)
        .filter(service => (selectedDirectorate === 'all' ? true : service.category?.directorate?.id === Number(selectedDirectorate)))
        .filter(service => (selectedCategory === 'all' ? true : service.category?.id === Number(selectedCategory)))
        .filter(service => (onlyElectronic ? service.isElectronic : true))
        .filter(service => {
          if (!search) {
            return true;
          }
          const term = search.toLowerCase();
          return (
            service.name?.toLowerCase().includes(term) ||
            service.code?.toLowerCase().includes(term) ||
            service.description?.toLowerCase().includes(term) ||
            service.category?.name?.toLowerCase().includes(term) ||
            service.category?.directorate?.name?.toLowerCase().includes(term)
          );
        }),
    [services, selectedDirectorate, selectedCategory, onlyElectronic, search],
  );

  const isLoading = directorateLoading || categoryLoading || serviceLoading;

  return (
    <div className="citizen-page">
      <div className="citizen-hero">
        <div className="hero-text">
          <p className="hero-kicker">بوابة المواطن</p>
          <h1>ابحث عن الخدمة الحكومية التي تحتاجها</h1>
          <p>تصفح الخدمات، الفئات والجهات بسهولة، واعرف المتطلبات قبل زيارتك.</p>
          <div className="hero-stats">
            <div className="hero-stat">
              <p>الجهات</p>
              <strong>{directorates.length}</strong>
            </div>
            <div className="hero-stat">
              <p>الفئات</p>
              <strong>{categories.length}</strong>
            </div>
            <div className="hero-stat">
              <p>الخدمات النشطة</p>
              <strong>{services.filter(service => service.active).length}</strong>
            </div>
          </div>
        </div>
        <div className="hero-brand">
          <img src="assets/images/logo.ai.svg" alt="شعار بوابة درعا" />
        </div>
      </div>

      <Row className="g-3 filters-layout">
        <Col lg="4">
          <div className="filters-card">
            <h3>تصفية الخدمات</h3>
            <div className="mb-3">
              <Label htmlFor="service-search" className="form-label">
                البحث
              </Label>
              <Input
                id="service-search"
                name="search"
                placeholder="ابحث باسم الخدمة أو الكود أو الوصف"
                value={search}
                onChange={event => setSearch(event.target.value)}
              />
            </div>
            <div className="mb-3">
              <Label htmlFor="directorate-filter" className="form-label">
                الجهة
              </Label>
              <Input
                type="select"
                id="directorate-filter"
                value={selectedDirectorate}
                onChange={event => setSelectedDirectorate(event.target.value)}
              >
                <option value="all">جميع الجهات</option>
                {directorates.map(directorate => (
                  <option key={directorate.id} value={directorate.id}>
                    {directorate.name}
                  </option>
                ))}
              </Input>
            </div>
            <div className="mb-3">
              <Label htmlFor="category-filter" className="form-label">
                الفئة
              </Label>
              <Input
                type="select"
                id="category-filter"
                value={selectedCategory}
                onChange={event => setSelectedCategory(event.target.value)}
              >
                <option value="all">جميع الفئات</option>
                {filteredCategories.map(category => (
                  <option key={category.id} value={category.id}>
                    {category.name}
                  </option>
                ))}
              </Input>
            </div>
            <div className="form-check form-switch d-flex align-items-center justify-content-between">
              <Label className="form-check-label" htmlFor="electronic-only">
                خدمات إلكترونية فقط
              </Label>
              <Input
                id="electronic-only"
                type="switch"
                className="form-check-input"
                checked={onlyElectronic}
                onChange={event => setOnlyElectronic(event.target.checked)}
              />
            </div>
          </div>
        </Col>

        <Col lg="8">
          {isLoading ? (
            <div className="loading-state">
              <Spinner color="primary" /> <span className="ms-2">جاري تحميل الخدمات...</span>
            </div>
          ) : filteredServices.length === 0 ? (
            <div className="empty-state">
              <p>لا توجد خدمات مطابقة للبحث أو الفلاتر.</p>
              <p className="text-muted">جرّب تغيير الفئة أو الجهة أو البحث عن كلمة أخرى.</p>
            </div>
          ) : (
            <div className="service-grid">
              {filteredServices.map(service => (
                <div key={service.id} className="service-card">
                  <p className="service-code">رمز الخدمة: {service.code}</p>
                  <h3>{service.name}</h3>
                  <p className="service-description">{service.description}</p>

                  <div className="service-badges">
                    {service.isElectronic && <Badge color="success">إلكترونية</Badge>}
                    {service.requiresPhysicalPresence && <Badge color="warning">تحتاج حضور</Badge>}
                    {service.hasSmartCard && <Badge color="info">تحتاج بطاقة ذكية</Badge>}
                  </div>

                  <div className="service-meta-row">
                    <div className="meta-pill">الجهة: {service.category?.directorate?.name ?? 'غير محددة'}</div>
                    <div className="meta-pill">الفئة: {service.category?.name ?? 'غير محددة'}</div>
                  </div>

                  <div className="service-actions">
                    <span className="text-muted small">المدة: {formatDuration(service)}</span>
                    <div className="d-flex align-items-center gap-2">
                      {service.feesDescription && <span className="fees">الرسوم: {service.feesDescription}</span>}
                      <Button tag={Link} to={`/citizen/services/${service.id}`} color="primary" size="sm">
                        تفاصيل الخدمة
                      </Button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CitizenLanding;
