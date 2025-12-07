import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './citizen-service.reducer';

export const CitizenService = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const citizenServiceList = useAppSelector(state => state.citizenService.entities);
  const loading = useAppSelector(state => state.citizenService.loading);
  const totalItems = useAppSelector(state => state.citizenService.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="citizen-service-heading" data-cy="CitizenServiceHeading">
        <Translate contentKey="citizenServicesApp.citizenService.home.title">Citizen Services</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="citizenServicesApp.citizenService.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/citizen-service/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="citizenServicesApp.citizenService.home.createLabel">Create new Citizen Service</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {citizenServiceList && citizenServiceList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="citizenServicesApp.citizenService.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('code')}>
                  <Translate contentKey="citizenServicesApp.citizenService.code">Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('code')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="citizenServicesApp.citizenService.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="citizenServicesApp.citizenService.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('estimatedDuration')}>
                  <Translate contentKey="citizenServicesApp.citizenService.estimatedDuration">Estimated Duration</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('estimatedDuration')} />
                </th>
                <th className="hand" onClick={sort('estimatedDurationUnit')}>
                  <Translate contentKey="citizenServicesApp.citizenService.estimatedDurationUnit">Estimated Duration Unit</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('estimatedDurationUnit')} />
                </th>
                <th className="hand" onClick={sort('requiresPhysicalPresence')}>
                  <Translate contentKey="citizenServicesApp.citizenService.requiresPhysicalPresence">Requires Physical Presence</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('requiresPhysicalPresence')} />
                </th>
                <th className="hand" onClick={sort('isElectronic')}>
                  <Translate contentKey="citizenServicesApp.citizenService.isElectronic">Is Electronic</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isElectronic')} />
                </th>
                <th className="hand" onClick={sort('hasSmartCard')}>
                  <Translate contentKey="citizenServicesApp.citizenService.hasSmartCard">Has Smart Card</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('hasSmartCard')} />
                </th>
                <th className="hand" onClick={sort('feesDescription')}>
                  <Translate contentKey="citizenServicesApp.citizenService.feesDescription">Fees Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('feesDescription')} />
                </th>
                <th className="hand" onClick={sort('active')}>
                  <Translate contentKey="citizenServicesApp.citizenService.active">Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('active')} />
                </th>
                <th>
                  <Translate contentKey="citizenServicesApp.citizenService.category">Category</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {citizenServiceList.map((citizenService, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/citizen-service/${citizenService.id}`} color="link" size="sm">
                      {citizenService.id}
                    </Button>
                  </td>
                  <td>{citizenService.code}</td>
                  <td>{citizenService.name}</td>
                  <td>{citizenService.description}</td>
                  <td>{citizenService.estimatedDuration}</td>
                  <td>
                    <Translate contentKey={`citizenServicesApp.EstimatedTimeUnit.${citizenService.estimatedDurationUnit}`} />
                  </td>
                  <td>{citizenService.requiresPhysicalPresence ? 'true' : 'false'}</td>
                  <td>{citizenService.isElectronic ? 'true' : 'false'}</td>
                  <td>{citizenService.hasSmartCard ? 'true' : 'false'}</td>
                  <td>{citizenService.feesDescription}</td>
                  <td>{citizenService.active ? 'true' : 'false'}</td>
                  <td>
                    {citizenService.category ? (
                      <Link to={`/service-category/${citizenService.category.id}`}>{citizenService.category.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/citizen-service/${citizenService.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/citizen-service/${citizenService.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/citizen-service/${citizenService.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="citizenServicesApp.citizenService.home.notFound">No Citizen Services found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={citizenServiceList && citizenServiceList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default CitizenService;
