import './App.css';
import ChatPanel from './components/ChatPanel';
import SummaryPanel from './components/SummaryPanel';
import StatusPanel from './components/StatusPanel';
import ModerationPanel from './components/ModerationPanel';

function App() {
  return (
    <div className="dashboard-container">
      <StatusPanel />
      <div className="main-panels">
        <ChatPanel />
        <div className="right-panels">
          <SummaryPanel />
          <ModerationPanel />
        </div>
      </div>
    </div>
  );
}

export default App;
