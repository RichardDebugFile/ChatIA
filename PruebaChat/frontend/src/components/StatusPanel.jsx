import React, { useEffect, useState } from 'react';
import './StatusPanel.css';

const GATEWAY_URL = 'http://localhost:8086/health';

const ICON_OK = (
  <span style={{fontSize: '1.3em', marginRight: '0.2em'}} role="img" aria-label="ok">🟢</span>
);
const ICON_FAIL = (
  <span style={{fontSize: '1.3em', marginRight: '0.2em'}} role="img" aria-label="fail">🔴</span>
);

function StatusPanel() {
  const [status, setStatus] = useState({});

  useEffect(() => {
    const fetchStatus = async () => {
      try {
        const res = await fetch(GATEWAY_URL);
        if (res.ok) {
          const data = await res.json();
          console.log('Respuesta del API Gateway:', data);
          setStatus(data);
        } else {
          console.log('Respuesta no OK del API Gateway');
          setStatus({});
        }
      } catch (e) {
        console.error('Error al consultar el API Gateway:', e);
        setStatus({});
      }
    };
    fetchStatus();
    const interval = setInterval(fetchStatus, 10000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="status-panel">
      <h2>Estado de Microservicios</h2>
      <div className="status-list">
        {Object.entries(status).map(([name, svcStatus]) => (
          <div key={name} className={svcStatus === 'UP' ? 'status-ok' : 'status-fail'}>
            {svcStatus === 'UP' ? ICON_OK : ICON_FAIL}
            {name}: {svcStatus === 'UP' ? 'Activo' : 'Inactivo'}
          </div>
        ))}
      </div>
    </div>
  );
}

export default StatusPanel; 