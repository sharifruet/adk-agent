import { useState } from 'react';
import { useConversation } from '../context/ConversationContext';
import './LeadCaptureForm.css';

const LeadCaptureForm = () => {
  const { submitCustomerInfo, setRequiresLeadCapture, isLoading } = useConversation();
  const [formData, setFormData] = useState({
    name: '',
    phone: '',
    email: '',
  });
  const [submitted, setSubmitted] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    // Basic validation
    if (!formData.name.trim() || !formData.phone.trim() || !formData.email.trim()) {
      setError('Please fill in all fields');
      return;
    }

    try {
      await submitCustomerInfo(formData);
      setSubmitted(true);
      setTimeout(() => {
        setRequiresLeadCapture(false);
      }, 3000);
    } catch (err) {
      setError(err.message || 'Failed to submit. Please try again.');
    }
  };

  const handleCancel = () => {
    setRequiresLeadCapture(false);
    setFormData({ name: '', phone: '', email: '' });
    setError(null);
  };

  if (submitted) {
    return (
      <div className="lead-capture-form success">
        <div className="success-message">
          <p>✓ Thank you! Your information has been submitted.</p>
          <p>A human agent will contact you soon.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="lead-capture-form">
      <div className="form-header">
        <h3>Contact Information</h3>
        <p>Please provide your details so we can connect you with an agent</p>
      </div>
      
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="name">Full Name *</label>
          <input
            type="text"
            id="name"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
            disabled={isLoading}
            placeholder="Enter your full name"
          />
        </div>

        <div className="form-group">
          <label htmlFor="phone">Phone Number *</label>
          <input
            type="tel"
            id="phone"
            name="phone"
            value={formData.phone}
            onChange={handleChange}
            required
            disabled={isLoading}
            placeholder="Enter your phone number"
          />
        </div>

        <div className="form-group">
          <label htmlFor="email">Email Address *</label>
          <input
            type="email"
            id="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
            disabled={isLoading}
            placeholder="Enter your email address"
          />
        </div>

        {error && (
          <div className="form-error">
            <p>{error}</p>
          </div>
        )}

        <div className="form-actions">
          <button type="submit" disabled={isLoading} className="submit-button">
            {isLoading ? 'Submitting...' : 'Submit'}
          </button>
          <button type="button" onClick={handleCancel} className="cancel-button" disabled={isLoading}>
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
};

export default LeadCaptureForm;

