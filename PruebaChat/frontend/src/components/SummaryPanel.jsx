import React, { useEffect, useState } from 'react';
import './SummaryPanel.css';

function SummaryPanel() {
  const [summary, setSummary] = useState(null);

  useEffect(() => {
    const fetchSummary = async () => {
      try {
        const res = await fetch('http://localhost:8085/api/summary/last');
        if (res.status === 204) {
          setSummary(null);
        } else {
          const data = await res.json();
          setSummary(data);
        }
      } catch (e) {
        setSummary(null);
      }
    };
    fetchSummary();
    const interval = setInterval(fetchSummary, 10000); // Actualiza cada 10s
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="summary-panel">
      <h2>Resumen IA (últimos 5 minutos)</h2>
      {summary ? (
        <>
          <div className="summary-content">{summary.summary}</div>
          {summary.infractions && summary.infractions.length > 0 && (
            <div className="summary-infractions">
              <b>Infracciones:</b>
              <ul>
                {summary.infractions.map((inf, idx) => (
                  <li key={idx}>{inf}</li>
                ))}
              </ul>
            </div>
          )}
        </>
      ) : (
        <div className="summary-empty">No hay resumen disponible.</div>
      )}
    </div>
  );
}

export default SummaryPanel; 