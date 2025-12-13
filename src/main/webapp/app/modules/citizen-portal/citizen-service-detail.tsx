import './citizen-portal.scss';

import React, { useEffect, useMemo } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Badge, Button, Card, CardBody, CardTitle, Col, Row, Spinner } from 'reactstrap';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity as getCitizenService } from 'app/entities/citizen-service/citizen-service.reducer';
import { getEntities as getRequiredDocuments } from 'app/entities/required-document/required-document.reducer';
import { getEntities as getServiceFormTemplates } from 'app/entities/service-form-template/service-form-template.reducer';
import { ICitizenService } from 'app/shared/model/citizen-service.model';
import { IRequiredDocument } from 'app/shared/model/required-document.model';
import { IServiceFormTemplate } from 'app/shared/model/service-form-template.model';
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

const buildDownloadLink = (template: IServiceFormTemplate) => {
  if (!template.file || !template.fileContentType) {
    return undefined;
  }
  return `data:${template.fileContentType};base64,${template.file}`;
};

const CitizenServicePublicDetail = () => {
  const { id } = useParams<'id'>();
  const dispatch = useAppDispatch();

  const service = useAppSelector(state => state.citizenService.entity);
  const serviceLoading = useAppSelector(state => state.citizenService.loading);
  const documents = useAppSelector(state => state.requiredDocument.entities);
  const documentsLoading = useAppSelector(state => state.requiredDocument.loading);
  const templates = useAppSelector(state => state.serviceFormTemplate.entities);
  const templatesLoading = useAppSelector(state => state.serviceFormTemplate.loading);

  useEffect(() => {
    if (id) {
      dispatch(getCitizenService(id));
      dispatch(getRequiredDocuments({ page: 0, size: 1000, sort: 'orderIndex,asc' }));
      dispatch(getServiceFormTemplates({ page: 0, size: 1000, sort: 'orderIndex,asc' }));
    }
  }, [id, dispatch]);

  const serviceDocuments = useMemo(
    () =>
      documents
        .filter((document: IRequiredDocument) => document.service?.id === service?.id)
        .sort((first, second) => (first.orderIndex ?? 0) - (second.orderIndex ?? 0)),
    [documents, service?.id],
  );

  const serviceTemplates = useMemo(
    () =>
      templates
        .filter((template: IServiceFormTemplate) => template.service?.id === service?.id)
        .sort((first, second) => (first.orderIndex ?? 0) - (second.orderIndex ?? 0)),
    [templates, service?.id],
  );

  const isLoading = serviceLoading || documentsLoading || templatesLoading;

  if (isLoading) {
    return (
      <div className="citizen-portal loading-state">
        <Spinner color="primary" /> <span>جاري تحميل الخدمة...</span>
      </div>
    );
  }

  if (!service || !service.id) {
    return (
      <div className="citizen-portal empty-state">
        <p>لم يتم العثور على الخدمة.</p>
        <Button tag={Link} to="/citizen" color="primary">
          عودة إلى الخدمات
        </Button>
      </div>
    );
  }

  return (
    <div className="citizen-portal">
      <div className="breadcrumb-bar">
        <div>
          <p className="eyebrow">وصول المواطن</p>
          <h1>{service.name}</h1>
          <p className="subtitle">{service.description}</p>
          <div className="service-meta">
            {service.isElectronic && <Badge color="success">إلكترونية</Badge>}
            {service.requiresPhysicalPresence && <Badge color="info">تحتاج حضور</Badge>}
            {service.hasSmartCard && <Badge color="warning">تحتاج بطاقة ذكية</Badge>}
          </div>
        </div>
        <div className="breadcrumb-bar__cta">
          <Button tag={Link} to="/citizen" color="link">
            رجوع لكل الخدمات
          </Button>
        </div>
      </div>

      <Row className="g-4 detail-layout">
        <Col lg="8">
          <Card className="detail-card">
            <CardBody>
              <Row>
                <Col md="6">
                  <p className="detail-label">الفئة</p>
                  <p className="detail-value">{service.category?.name ?? 'غير محددة'}</p>
                </Col>
                <Col md="6">
                  <p className="detail-label">الجهة</p>
                  <p className="detail-value">{service.category?.directorate?.name ?? 'غير محددة'}</p>
                </Col>
              </Row>
              <Row className="mt-3">
                <Col md="6">
                  <p className="detail-label">المدة التقديرية</p>
                  <p className="detail-value">{formatDuration(service)}</p>
                </Col>
                <Col md="6">
                  <p className="detail-label">الرسوم</p>
                  <p className="detail-value">{service.feesDescription ?? 'لا توجد رسوم محددة'}</p>
                </Col>
              </Row>
            </CardBody>
          </Card>

          <Card className="detail-card">
            <CardBody>
              <CardTitle tag="h3">المستندات المطلوبة</CardTitle>
              {serviceDocuments.length === 0 && <p className="text-muted mb-0">لا توجد مستندات مرتبطة بالخدمة بعد.</p>}
              {serviceDocuments.length > 0 && (
                <ul className="item-list">
                  {serviceDocuments.map(document => (
                    <li key={document.id}>
                      <div>
                        <strong>{document.name}</strong>
                        {document.description && <p className="text-muted mb-0">{document.description}</p>}
                      </div>
                      {document.mandatory && <Badge color="danger">مطلوب</Badge>}
                    </li>
                  ))}
                </ul>
              )}
            </CardBody>
          </Card>

          <Card className="detail-card">
            <CardBody>
              <CardTitle tag="h3">نماذج الخدمة</CardTitle>
              {serviceTemplates.length === 0 && <p className="text-muted mb-0">لا توجد نماذج منشورة لهذه الخدمة.</p>}
              {serviceTemplates.length > 0 && (
                <ul className="item-list">
                  {serviceTemplates.map(template => (
                    <li key={template.id}>
                      <div>
                        <strong>{template.name}</strong>
                        {template.description && <p className="text-muted mb-0">{template.description}</p>}
                      </div>
                      {buildDownloadLink(template) ? (
                        <Button tag="a" color="primary" outline size="sm" href={buildDownloadLink(template)} download={template.name}>
                          تحميل
                        </Button>
                      ) : (
                        <Badge color="secondary">لا يوجد ملف</Badge>
                      )}
                    </li>
                  ))}
                </ul>
              )}
            </CardBody>
          </Card>
        </Col>
        <Col lg="4">
          <Card className="detail-card highlight">
            <CardBody>
              <CardTitle tag="h3">طريقة تنفيذ الخدمة</CardTitle>
              <ul className="pill-list">
                <li>
                  <span className="pill-label">متاحة إلكترونياً</span>
                  <span className="pill-value">{service.isElectronic ? 'نعم' : 'لا'}</span>
                </li>
                <li>
                  <span className="pill-label">حضور فعلي</span>
                  <span className="pill-value">{service.requiresPhysicalPresence ? 'مطلوب' : 'غير مطلوب'}</span>
                </li>
                <li>
                  <span className="pill-label">بطاقة ذكية</span>
                  <span className="pill-value">{service.hasSmartCard ? 'مطلوبة' : 'غير مطلوبة'}</span>
                </li>
              </ul>
              <div className="side-help">
                <p className="eyebrow">تحتاج مساعدة؟</p>
                <p className="text-muted">يمكنك زيارة الجهة المعنية أو التواصل مع فريق البوابة للحصول على إرشادات أكثر.</p>
                <Button tag={Link} to="/login" color="secondary" outline size="sm">
                  دخول المسؤول
                </Button>
              </div>
            </CardBody>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default CitizenServicePublicDetail;
