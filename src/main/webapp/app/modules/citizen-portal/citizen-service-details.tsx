import './citizen-portal.scss';

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link, useParams } from 'react-router-dom';
import { Alert, Badge, Button, Col, Row, Spinner } from 'reactstrap';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ICitizenService } from 'app/shared/model/citizen-service.model';
import { IRequiredDocument } from 'app/shared/model/required-document.model';
import { IServiceFormTemplate } from 'app/shared/model/service-form-template.model';

const CitizenServiceDetails = () => {
  const { id } = useParams();
  const [service, setService] = useState<ICitizenService | null>(null);
  const [documents, setDocuments] = useState<IRequiredDocument[]>([]);
  const [templates, setTemplates] = useState<IServiceFormTemplate[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchService = async () => {
      if (!id) {
        return;
      }

      setLoading(true);
      setError(null);
      try {
        const [serviceResponse, documentsResponse, templatesResponse] = await Promise.all([
          axios.get<ICitizenService>(`api/citizen-services/${id}`),
          axios.get<IRequiredDocument[]>(`api/required-documents?serviceId.equals=${id}&sort=orderIndex,asc`),
          axios.get<IServiceFormTemplate[]>(`api/service-form-templates?serviceId.equals=${id}&sort=orderIndex,asc`),
        ]);

        setService(serviceResponse.data);
        setDocuments(documentsResponse.data);
        setTemplates(templatesResponse.data.filter(template => template.active));
      } catch (err) {
        setError('citizenPortal.errors.loadService');
      } finally {
        setLoading(false);
      }
    };

    fetchService();
  }, [id]);

  const renderDuration = () => {
    if (!service?.estimatedDuration || !service.estimatedDurationUnit) {
      return null;
    }

    return `${service.estimatedDuration} ${translate(`citizenPortal.timeUnits.${service.estimatedDurationUnit.toLowerCase()}`)}`;
  };

  return (
    <div className="citizen-page">
      <div className="detail-page-header">
        <div>
          <p className="text-muted mb-1">العودة إلى بوابة المواطن</p>
          <h1 className="h3 mb-0">{service?.name || translate('citizenPortal.loading')}</h1>
        </div>
        <Link to="/citizen" className="btn btn-outline-secondary d-flex align-items-center gap-1">
          <FontAwesomeIcon icon="arrow-left" />
          <span>رجوع للخدمات</span>
        </Link>
      </div>

      {loading && (
        <div className="text-center my-5">
          <Spinner color="primary" />
        </div>
      )}

      {error && <Alert color="danger">{translate(error)}</Alert>}

      {!loading && service && (
        <>
          <Row className="g-3 service-section">
            <Col md="8">
              <div className="service-detail-card">
                <h2 className="h5 mb-2">نبذة عن الخدمة</h2>
                <p className="text-muted mb-3">{service.description}</p>

                <div className="d-flex flex-wrap gap-2 mb-2">
                  <Badge color="light" className="text-primary">
                    رمز الخدمة: {service.code}
                  </Badge>
                  {renderDuration() && (
                    <Badge color="info">
                      <FontAwesomeIcon icon="clock" />
                      <span className="ms-1">{renderDuration()}</span>
                    </Badge>
                  )}
                  {service.isElectronic && <Badge color="success">إلكترونية</Badge>}
                  {service.requiresPhysicalPresence && <Badge color="warning">تحتاج حضور</Badge>}
                  {service.hasSmartCard && <Badge color="info">تحتاج بطاقة ذكية</Badge>}
                </div>

                {service.feesDescription && (
                  <p className="mb-0">
                    <strong>الرسوم:</strong> {service.feesDescription}
                  </p>
                )}
              </div>

              <div className="service-detail-card">
                <h2 className="h5 mb-3">المستندات المطلوبة</h2>
                {documents.length === 0 ? (
                  <p className="text-muted mb-0">لا توجد مستندات مرتبطة بهذه الخدمة حالياً.</p>
                ) : (
                  <ul className="service-list">
                    {documents.map(document => (
                      <li key={document.id}>
                        <div className="d-flex justify-content-between align-items-start gap-2">
                          <div>
                            <p className="fw-semibold mb-1">{document.name}</p>
                            {document.description && <p className="text-muted mb-1">{document.description}</p>}
                          </div>
                          {document.mandatory ? <Badge color="danger">مطلوب</Badge> : <Badge color="secondary">اختياري</Badge>}
                        </div>
                      </li>
                    ))}
                  </ul>
                )}
              </div>
            </Col>
            <Col md="4">
              <div className="service-detail-card">
                <h2 className="h5 mb-2">خطوات الاستفادة</h2>
                <p className="text-muted">اتبع هذه الخطوات السريعة للوصول للخدمة بالطريقة الصحيحة.</p>
                <ul className="service-list">
                  <li>
                    <FontAwesomeIcon icon="search" className="text-primary" />
                    <span className="ms-2">استعرض الخدمة ومتطلباتها.</span>
                  </li>
                  <li>
                    <FontAwesomeIcon icon="save" className="text-primary" />
                    <span className="ms-2">حضّر المستندات والنماذج.</span>
                  </li>
                  <li>
                    <FontAwesomeIcon icon="road" className="text-primary" />
                    <span className="ms-2">قدّم الطلب للجهة المختصة.</span>
                  </li>
                </ul>
                {service.isElectronic && (
                  <Alert color="success" className="mt-3 mb-0">
                    هذه الخدمة متاحة إلكترونياً ويمكن إنجازها بالكامل دون زيارة.
                  </Alert>
                )}
              </div>

              <div className="service-detail-card">
                <h2 className="h5 mb-2">النماذج والملفات</h2>
                {templates.length === 0 ? (
                  <p className="text-muted mb-0">لا توجد نماذج مرفوعة لهذه الخدمة حتى الآن.</p>
                ) : (
                  <ul className="service-list">
                    {templates.map(template => (
                      <li key={template.id}>
                        <div className="d-flex justify-content-between align-items-center">
                          <div>
                            <p className="fw-semibold mb-0">{template.name}</p>
                            {template.description && <small className="text-muted">{template.description}</small>}
                          </div>
                          {template.file && template.fileContentType && (
                            <Button
                              tag="a"
                              color="link"
                              className="p-0"
                              href={`data:${template.fileContentType};base64,${template.file}`}
                              download={template.name}
                            >
                              <FontAwesomeIcon icon="download" />
                              <span className="ms-1">تحميل</span>
                            </Button>
                          )}
                        </div>
                      </li>
                    ))}
                  </ul>
                )}
              </div>
            </Col>
          </Row>
        </>
      )}
    </div>
  );
};

export default CitizenServiceDetails;
