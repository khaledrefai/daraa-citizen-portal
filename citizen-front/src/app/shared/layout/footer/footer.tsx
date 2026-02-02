import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import './footer.scss';

export function Footer() {
  return (
    <footer className="footer-gradient footer-main">
      <div className="container py-5">
        <div className="footer-content-wrapper">
          {/* 1. About Section - شعار المنصة */}
          <div className="footer-section footer-about">
            <h5 className="footer-title">منصة خدمات المواطن</h5>
            <p className="footer-subtitle">محافظة درعا</p>
          </div>
          {/* 2. Social Media - مواقع التواصل */}
          <div className="footer-section footer-social-section">
            <h5 className="footer-title">تابعنا على</h5>
            <div className="social-icons-list">
              <a href="https://www.facebook.com/share/1HEy3zss2U/" className="social-icon" aria-label="Facebook">
                <FontAwesomeIcon icon={['fab', 'facebook-f']} />
              </a>
              <a href="https://x.com/daraagov1" className="social-icon" aria-label="X">
                <span className="x-text">X</span>
              </a>
              <a href="https://www.instagram.com/daraagov1?igsh=MWYzbGc2OG0xeGNwNA==" className="social-icon" aria-label="Instagram">
                <FontAwesomeIcon icon={['fab', 'instagram']} />
              </a>
            </div>
          </div>

          {/* 3. Contact Info - معلومات التواصل */}
          <div className="footer-section footer-contact-section">
            <h5 className="footer-title">معلومات التواصل</h5>
            <ul className="contact-list">
              <li>
                <FontAwesomeIcon icon="location-dot" className="icon" />
                <span>محافظة درعا، سوريا</span>
              </li>
              <li>
                <FontAwesomeIcon icon="envelope" className="icon" />
                <span>info@daraa.gov.sy</span>
              </li>
            </ul>
          </div>
        </div>

        <hr className="footer-divider" />

        <div className="footer-bottom text-center">
          <p>جميع الحقوق محفوظة © 2026 - محافظة درعا</p>
          <p className="developer-tag">تم التطوير بواسطة قسم تكنولوجيا المعلومات - محافظة درعا</p>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
