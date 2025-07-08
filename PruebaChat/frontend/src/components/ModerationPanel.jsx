import React, { useEffect, useState } from 'react';
import './ModerationPanel.css';

const MODERATOR_URL = 'http://localhost:8087/api/moderator/status';

function ModerationPanel() {
  const [moderationStatus, setModerationStatus] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchModerationStatus = async () => {
      try {
        const res = await fetch(MODERATOR_URL);
        if (res.ok) {
          const data = await res.json();
          setModerationStatus(data);
        } else {
          console.error('Error al obtener estado de moderación');
          setModerationStatus(null);
        }
      } catch (e) {
        console.error('Error al consultar el moderador:', e);
        setModerationStatus(null);
      } finally {
        setLoading(false);
      }
    };

    fetchModerationStatus();
    const interval = setInterval(fetchModerationStatus, 10000); // Actualiza cada 10s
    return () => clearInterval(interval);
  }, []);

  if (loading) {
    return (
      <div className="moderation-panel">
        <h2>Estado de Moderación</h2>
        <div className="moderation-loading">Cargando...</div>
      </div>
    );
  }

  if (!moderationStatus) {
    return (
      <div className="moderation-panel">
        <h2>Estado de Moderación</h2>
        <div className="moderation-error">No se pudo conectar con el moderador</div>
      </div>
    );
  }

  return (
    <div className="moderation-panel">
      <h2>Estado de Moderación</h2>
      
      <div className="moderation-stats">
        <div className="stat-item">
          <span className="stat-label">Mensajes escaneados:</span>
          <span className="stat-value">{moderationStatus.messagesScanned}</span>
        </div>
        <div className="stat-item">
          <span className="stat-label">Última revisión:</span>
          <span className="stat-value">
            {moderationStatus.lastCheck ? 
              new Date(moderationStatus.lastCheck).toLocaleTimeString() : 
              'N/A'
            }
          </span>
        </div>
        <div className="stat-item">
          <span className="stat-label">Estado:</span>
          <span className={`stat-value ${moderationStatus.hasViolations ? 'has-violations' : 'clean'}`}>
            {moderationStatus.hasViolations ? '⚠️ Infracciones detectadas' : '✅ Todo limpio'}
          </span>
        </div>
      </div>

      {moderationStatus.hasViolations && moderationStatus.violations && (
        <div className="violations-section">
          <h3>Infracciones Recientes</h3>
          <div className="violations-list">
            {moderationStatus.violations.slice(-5).map((violation, idx) => (
              <div key={idx} className="violation-item">
                <div className="violation-header">
                  <span className="violation-user">{violation.username}</span>
                  <span className="violation-platform">[{violation.platform}]</span>
                  <span className="violation-time">
                    {new Date(violation.timestamp).toLocaleTimeString()}
                  </span>
                </div>
                <div className="violation-message">{violation.message}</div>
              </div>
            ))}
          </div>
        </div>
      )}

      {(!moderationStatus.hasViolations || !moderationStatus.violations || moderationStatus.violations.length === 0) && (
        <div className="moderation-clean">
          <span className="clean-icon">✅</span>
          <p>No se han detectado infracciones recientes</p>
        </div>
      )}
    </div>
  );
}

export default ModerationPanel; 