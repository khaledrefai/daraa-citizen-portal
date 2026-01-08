import './footer.scss';

import React from 'react';
import { Col, Row } from 'reactstrap';

const Footer = () => (
  <div className="footer page-content">
    <Row className="align-items-center">
      <Col md="8" className="d-flex gap-3 align-items-center footer-brand">
        <img src="assets/images/logo.ai.svg" alt="شعار بوابة درعا" className="footer-logo" />
        <div>
          <p className="mb-1 fw-bold">بوابة خدمات محافظة درعا</p>
          <p className="mb-0 footer-subtitle">واجهة عربية كاملة، وتجربة مصممة من اليمين إلى اليسار لكل الخدمات</p>
        </div>
      </Col>
      <Col md="4" className="text-md-start text-lg-start text-center">
        <p className="mb-0 footer-copy">© {new Date().getFullYear()} محافظة درعا - جميع الحقوق محفوظة</p>
      </Col>
    </Row>
  </div>
);

export default Footer;
