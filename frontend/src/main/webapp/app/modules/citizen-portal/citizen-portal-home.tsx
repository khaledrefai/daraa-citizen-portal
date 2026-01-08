import './citizen-portal.scss';

import React, { useEffect, useMemo, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { Input, Spinner } from 'reactstrap';
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

const CitizenPortalHome = () => {
  const [services, setServices] = useState<ICitizenService[]>([]);
  const [categories, setCategories] = useState<IServiceCategory[]>([]);
  const [directorates, setDirectorates] = useState<IDirectorate[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedDirectorate, setSelectedDirectorate] = useState<string>('all');
  const [onlyElectronic, setOnlyElectronic] = useState(false);
  const [expandedCategories, setExpandedCategories] = useState<Set<number>>(new Set());

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
        const allCategoryIds = new Set<number>();
        categoriesResponse.data.forEach(cat => {
          if (cat.id !== undefined) {
            allCategoryIds.add(cat.id);
          }
        });
        setExpandedCategories(allCategoryIds);
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
  };

  const toggleCategory = (categoryId: number) => {
    setExpandedCategories(prev => {
      const newSet = new Set(prev);
      if (newSet.has(categoryId)) {
        newSet.delete(categoryId);
      } else {
        newSet.add(categoryId);
      }
      return newSet;
    });
  };

  const filteredServices = useMemo(() => {
    const term = searchTerm.trim().toLowerCase();
    return services
      .filter(service => service.active)
      .filter(service => (selectedDirectorate === 'all' ? true : service.category?.directorate?.id === Number(selectedDirectorate)))
      .filter(service => (onlyElectronic ? service.isElectronic : true))
      .filter(service =>
        !term
          ? true
          : [service.name, service.description, service.category?.name, service.category?.directorate?.name, service.code].some(value =>
              value?.toLowerCase().includes(term),
            ),
      );
  }, [services, selectedDirectorate, onlyElectronic, searchTerm]);

  const servicesByCategory = useMemo(() => {
    const grouped = new Map<number, { category: IServiceCategory; services: ICitizenService[] }>();

    filteredServices.forEach(service => {
      const category = service.category;
      const categoryId = category?.id;
      if (categoryId !== undefined && category) {
        const existing = grouped.get(categoryId);
        if (existing) {
          existing.services.push(service);
        } else {
          grouped.set(categoryId, { category, services: [service] });
        }
      }
    });

    return Array.from(grouped.values()).sort((a, b) => (a.category.name ?? '').localeCompare(b.category.name ?? '', 'ar'));
  }, [filteredServices]);

  const formatDuration = (service: ICitizenService) => {
    if (!service.estimatedDuration || !service.estimatedDurationUnit) {
      return 'غير محددة';
    }
    return `${service.estimatedDuration} ${durationLabels[service.estimatedDurationUnit] ?? translate('citizenPortal.timeUnits.minute')}`;
  };

  const selectedDirectorateData = directorates.find(d => d.id?.toString() === selectedDirectorate);
  const totalServicesCount = filteredServices.length;

  const renderServiceCard = (service: ICitizenService) => (
    <article key={service.id} className="service-card-v2">
      <div className="card-header">
        <span className="service-code">{service.code}</span>
        <div className="service-tags">
          {service.isElectronic && (
            <span className="tag tag-electronic">
              <FontAwesomeIcon icon="bolt" /> إلكترونية
            </span>
          )}
          {service.requiresPhysicalPresence && (
            <span className="tag tag-presence">
              <FontAwesomeIcon icon="user" /> حضور شخصي
            </span>
          )}
          {service.hasSmartCard && (
            <span className="tag tag-smartcard">
              <FontAwesomeIcon icon="id-card" /> بطاقة ذكية
            </span>
          )}
        </div>
      </div>

      <h3 className="service-title">{service.name}</h3>
      <p className="service-desc">{service.description}</p>

      <div className="card-meta">
        <div className="meta-item">
          <FontAwesomeIcon icon="clock" />
          <span>{formatDuration(service)}</span>
        </div>
        {service.feesDescription && (
          <div className="meta-item meta-fees">
            <FontAwesomeIcon icon="coins" />
            <span>{service.feesDescription}</span>
          </div>
        )}
      </div>

      <Link to={`/citizen/services/${service.id}`} className="card-action">
        <span>عرض التفاصيل</span>
        <FontAwesomeIcon icon="arrow-left" />
      </Link>
    </article>
  );

  const renderCategorySection = (categoryData: { category: IServiceCategory; services: ICitizenService[] }) => {
    const { category, services: categoryServices } = categoryData;
    const categoryId = category.id ?? 0;
    const isExpanded = expandedCategories.has(categoryId);

    return (
      <div key={categoryId} className="category-section">
        <button type="button" className="category-header" onClick={() => toggleCategory(categoryId)}>
          <div className="category-info">
            <FontAwesomeIcon icon="folder" className="category-icon" />
            <h2 className="category-name">{category.name}</h2>
            <span className="category-count">{categoryServices.length} خدمة</span>
          </div>
          <FontAwesomeIcon icon={isExpanded ? 'chevron-up' : 'chevron-down'} className="expand-icon" />
        </button>

        {isExpanded && <div className="services-grid">{categoryServices.map(renderServiceCard)}</div>}
      </div>
    );
  };

  return (
    <div className="citizen-portal-v2">
      <nav className="directorate-nav">
        <div className="nav-brand">
          <img src="assets/images/logo.ai.svg" alt="بوابة درعا" className="nav-logo" />
          <div className="nav-title">
            <span className="title-main">دليل الخدمات</span>
            <span className="title-sub">محافظة درعا</span>
          </div>
        </div>

        <div className="directorate-tabs-wrapper">
          <div className="directorate-tabs">
            <button
              type="button"
              className={`dir-tab ${selectedDirectorate === 'all' ? 'active' : ''}`}
              onClick={() => handleDirectorateSelect('all')}
            >
              <FontAwesomeIcon icon="layer-group" />
              <span>جميع الجهات</span>
              <span className="tab-count">{services.filter(s => s.active).length}</span>
            </button>
            {directorates.map(directorate => {
              const count = services.filter(s => s.category?.directorate?.id === directorate.id && s.active).length;
              return (
                <button
                  type="button"
                  key={directorate.id}
                  className={`dir-tab ${selectedDirectorate === directorate.id?.toString() ? 'active' : ''}`}
                  onClick={() => handleDirectorateSelect(directorate.id?.toString() ?? 'all')}
                >
                  <FontAwesomeIcon icon="building-columns" />
                  <span>{directorate.name}</span>
                  <span className="tab-count">{count}</span>
                </button>
              );
            })}
          </div>
        </div>
      </nav>

      <div className="search-filter-bar">
        <div className="search-container">
          <FontAwesomeIcon icon="magnifying-glass" className="search-icon" />
          <Input
            type="search"
            className="search-input"
            placeholder="ابحث عن خدمة... (اسم، رمز، وصف)"
            value={searchTerm}
            onChange={e => setSearchTerm(e.target.value)}
          />
          {searchTerm && (
            <button type="button" className="search-clear" onClick={() => setSearchTerm('')}>
              <FontAwesomeIcon icon="xmark" />
            </button>
          )}
        </div>

        <div className="filter-options">
          <label className="filter-toggle">
            <input type="checkbox" checked={onlyElectronic} onChange={e => setOnlyElectronic(e.target.checked)} />
            <span className="toggle-slider"></span>
            <span className="toggle-label">
              <FontAwesomeIcon icon="bolt" />
              إلكترونية فقط
            </span>
          </label>

          <div className="results-summary">
            <span className="summary-count">{totalServicesCount}</span>
            <span className="summary-label">خدمة</span>
            {selectedDirectorateData && (
              <span className="summary-filter">
                في <strong>{selectedDirectorateData.name}</strong>
              </span>
            )}
          </div>
        </div>
      </div>

      <main className="services-content">
        {error && (
          <div className="error-banner">
            <FontAwesomeIcon icon="circle-exclamation" />
            <span>{translate(error)}</span>
          </div>
        )}

        {loading ? (
          <div className="loading-container">
            <Spinner color="primary" />
            <span>جاري تحميل الخدمات...</span>
          </div>
        ) : servicesByCategory.length === 0 ? (
          <div className="empty-container">
            <FontAwesomeIcon icon="folder-open" className="empty-icon" />
            <h3>لا توجد خدمات مطابقة</h3>
            <p>جرّب تغيير الجهة أو كلمة البحث</p>
          </div>
        ) : (
          <div className="categories-container">{servicesByCategory.map(renderCategorySection)}</div>
        )}
      </main>

      <CitizenAssistant />
    </div>
  );
};

export default CitizenPortalHome;
