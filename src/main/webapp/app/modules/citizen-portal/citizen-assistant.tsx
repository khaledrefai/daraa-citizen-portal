import './citizen-portal.scss';

import React, { FormEvent, useEffect, useMemo, useRef, useState } from 'react';
import axios from 'axios';
import { Button, Form, Input, Spinner } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

interface AssistantSource {
  serviceId: number;
  name: string;
  description?: string;
  score?: number;
}

interface AssistantChatResponse {
  answer: string;
  sources: AssistantSource[];
}

type ChatMessage = {
  from: 'user' | 'assistant';
  text: string;
  sources?: AssistantSource[];
};

const CitizenAssistant = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [question, setQuestion] = useState('');
  const [messages, setMessages] = useState<ChatMessage[]>([
    {
      from: 'assistant',
      text: 'أنا مساعد درعا للرد على أسئلتك حول الخدمات المتاحة. اكتب سؤالك لأساعدك.\nمثال: كيف أجدد بطاقة الهوية؟',
    },
  ]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const chatBodyRef = useRef<HTMLDivElement | null>(null);

  const isDisabled = useMemo(() => loading || !question.trim(), [loading, question]);

  useEffect(() => {
    if (chatBodyRef.current) {
      chatBodyRef.current.scrollTop = chatBodyRef.current.scrollHeight;
    }
  }, [messages, isOpen]);

  const handleSend = async (event: FormEvent) => {
    event.preventDefault();
    if (!question.trim()) {
      return;
    }

    const userMessage: ChatMessage = { from: 'user', text: question.trim() };
    setMessages(prev => [...prev, userMessage]);
    setError(null);
    setLoading(true);
    setQuestion('');

    try {
      const response = await axios.post<AssistantChatResponse>('api/assistant/chat', { question: userMessage.text });
      const assistantMessage: ChatMessage = { from: 'assistant', text: response.data.answer, sources: response.data.sources };
      setMessages(prev => [...prev, assistantMessage]);
    } catch (err) {
      setError('تعذر إرسال السؤال حالياً. يرجى المحاولة مجدداً.');
    } finally {
      setLoading(false);
    }
  };

  const renderSources = (sources?: AssistantSource[]) => {
    if (!sources || sources.length === 0) {
      return null;
    }

    return (
      <div className="assistant-sources">
        <p className="mb-1 fw-semibold">اعتمدت على الخدمات التالية:</p>
        <ul>
          {sources.map(source => (
            <li key={source.serviceId}>
              <strong>{source.name}</strong>
              {source.description && <span className="text-muted"> — {source.description}</span>}
            </li>
          ))}
        </ul>
      </div>
    );
  };

  return (
    <div className={`citizen-assistant ${isOpen ? 'open' : ''}`}>
      <Button color="primary" className="assistant-toggle" onClick={() => setIsOpen(!isOpen)} aria-expanded={isOpen}>
        <FontAwesomeIcon icon={isOpen ? 'times' : 'comments'} />
        <span className="ms-2">مساعد الخدمات</span>
      </Button>

      {isOpen && (
        <div className="assistant-card shadow">
          <div className="assistant-header">
            <div className="d-flex align-items-center gap-2">
              <FontAwesomeIcon icon="robot" />
              <div>
                <p className="mb-0 fw-semibold">مساعد بوابة المواطن</p>
                <small className="text-muted">يبحث في الخدمات ويجيب فوراً</small>
              </div>
            </div>
            <Button size="sm" color="link" className="text-reset" onClick={() => setIsOpen(false)} aria-label="إغلاق المحادثة">
              <FontAwesomeIcon icon="times" />
            </Button>
          </div>

          <div className="assistant-body" ref={chatBodyRef}>
            {messages.map((message, index) => (
              <div key={`${message.from}-${index}`} className={`assistant-message ${message.from}`}>
                <div className="message-bubble">
                  {message.text.split('\n').map((line, lineIndex) => (
                    <p key={lineIndex} className="mb-1">
                      {line}
                    </p>
                  ))}
                </div>
                {message.from === 'assistant' && renderSources(message.sources)}
              </div>
            ))}
            {loading && (
              <div className="assistant-message assistant">
                <div className="message-bubble d-flex align-items-center gap-2">
                  <Spinner size="sm" />
                  <span>يجري التفكير...</span>
                </div>
              </div>
            )}
          </div>

          <Form onSubmit={handleSend} className="assistant-form">
            <Input
              type="text"
              value={question}
              placeholder="اكتب سؤالك هنا"
              onChange={event => setQuestion(event.target.value)}
              disabled={loading}
            />
            <Button color="primary" type="submit" disabled={isDisabled} aria-label="إرسال السؤال">
              {loading ? <Spinner size="sm" /> : <FontAwesomeIcon icon="paper-plane" />}
            </Button>
          </Form>

          {error && <p className="text-danger mt-2 mb-0 small">{error}</p>}
        </div>
      )}
    </div>
  );
};

export default CitizenAssistant;
