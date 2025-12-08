import './citizen-portal.scss';

import React, { useEffect, useMemo, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { Alert, Badge, Card, CardBody, Col, Input, Row, Spinner } from 'reactstrap';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ICitizenService } from 'app/shared/model/citizen-service.model';
import { IServiceCategory } from 'app/shared/model/service-category.model';
import { IDirectorate } from 'app/shared/model/directorate.model';

interface GroupedCategory extends IServiceCategory {
  services: ICitizenService[];
}

const CitizenPortalHome = () => {
  const [services, setServices] = useState<ICitizenService[]>([]);
  const [categories, setCategories] = useState<IServiceCategory[]>([]);
  const [directorates, setDirectorates] = useState<IDirectorate[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [searchTerm, setSearchTerm] = useState('');

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

  const filteredServices = useMemo(() => {
    const term = searchTerm.trim().toLowerCase();
    if (!term) {
      return services;
    }
    return services.filter(service =>
      [service.name, service.description, service.category?.name].some(value => value?.toLowerCase().includes(term)),
    );
  }, [services, searchTerm]);

  const groupedCategories: GroupedCategory[] = useMemo(() => {
    const categoryMap = new Map<number, GroupedCategory>();

    categories.forEach(category => {
      if (category.id !== undefined) {
        categoryMap.set(category.id, { ...category, services: [] });
      }
    });

    filteredServices.forEach(service => {
      const categoryId = service.category?.id;
      if (categoryId && categoryMap.has(categoryId)) {
        categoryMap.get(categoryId).services.push(service);
      }
    });

    return Array.from(categoryMap.values()).filter(category => category.services.length > 0);
  }, [categories, filteredServices]);

  const getDirectorateName = (category: IServiceCategory) =>
    directorates.find(directorate => directorate.id === category.directorate?.id)?.name || category.directorate?.name;

  return (
    <div className="citizen-page">
      <div className="citizen-hero">
        <h1 className="h3 mb-2">
          <Translate contentKey="citizenPortal.title" />
        </h1>
        <p className="text-muted mb-3">
          <Translate contentKey="citizenPortal.subtitle" />
        </p>
        <div className="citizen-search">
          <Input
            type="search"
            value={searchTerm}
            onChange={event => setSearchTerm(event.target.value)}
            placeholder={translate('citizenPortal.searchPlaceholder')}
            aria-label={translate('citizenPortal.searchPlaceholder')}
          />
        </div>
      </div>

      {loading && (
        <div className="text-center my-4">
          <Spinner color="primary" />
        </div>
      )}

      {error && (
        <Alert color="danger" className="mt-3">
          <Translate contentKey={error} />
        </Alert>
      )}

      {!loading && groupedCategories.length === 0 && !error ? (
        <Alert color="info">
          <Translate contentKey="citizenPortal.empty" />
        </Alert>
      ) : (
        <Row className="g-3">
          {groupedCategories.map(category => (
            <Col md="6" lg="4" key={category.id}>
              <Card className="category-card h-100">
                <CardBody>
                  <h3 className="h5">
                    <FontAwesomeIcon icon="th-list" className="text-primary" />
                    <span>{category.name}</span>
                  </h3>
                  {getDirectorateName(category) && (
                    <p className="service-meta mb-2">
                      <FontAwesomeIcon icon="building" /> <Translate contentKey="citizenPortal.directorate" />:{' '}
                      {getDirectorateName(category)}
                    </p>
                  )}
                  <div>
                    {category.services.map(service => (
                      <div className="service-chip" key={service.id}>
                        <div className="d-flex justify-content-between align-items-start">
                          <div>
                            <p className="fw-semibold mb-1">{service.name}</p>
                            <p className="service-meta mb-2">{service.description}</p>
                          </div>
                          <Badge color="light" className="text-primary">
                            <Translate contentKey="citizenPortal.serviceCode" interpolate={{ code: service.code }} />
                          </Badge>
                        </div>
                        <div className="d-flex flex-wrap gap-1 mb-2">
                          {service.isElectronic && (
                            <Badge color="success">
                              <Translate contentKey="citizenPortal.flags.electronic" />
                            </Badge>
                          )}
                          {service.requiresPhysicalPresence && (
                            <Badge color="warning" pill>
                              <Translate contentKey="citizenPortal.flags.inPerson" />
                            </Badge>
                          )}
                          {service.hasSmartCard && (
                            <Badge color="info" pill>
                              <Translate contentKey="citizenPortal.flags.smartCard" />
                            </Badge>
                          )}
                        </div>
                        <div className="citizen-actions">
                          <Link to={`/citizen/services/${service.id}`} className="btn btn-outline-primary btn-sm">
                            <FontAwesomeIcon icon="eye" />
                            <span className="ms-2">
                              <Translate contentKey="citizenPortal.actions.details" />
                            </span>
                          </Link>
                          {service.feesDescription && (
                            <Badge color="secondary" pill>
                              <Translate contentKey="citizenPortal.fees" /> {service.feesDescription}
                            </Badge>
                          )}
                        </div>
                      </div>
                    ))}
                  </div>
                </CardBody>
              </Card>
            </Col>
          ))}
        </Row>
      )}
    </div>
  );
};

export default CitizenPortalHome;
