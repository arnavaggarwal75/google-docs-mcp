import React from 'react';
import { useState, useEffect } from 'react';

const Dashboard = () => {
  const [videoUrl, setVideoUrl] = useState("");
  const [notes, setNotes] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async () => {
    if (!videoUrl.trim()) return;
    setLoading(true);
    setNotes("");

    console.log("testing");
    
    setLoading(false);
  };

  useEffect(() => {
    const styleSheet = document.createElement("style");
    styleSheet.type = "text/css";
    styleSheet.innerText = styles;
    document.head.appendChild(styleSheet);
    return () => {
      document.head.removeChild(styleSheet);
    };
  }, []);
  
  return (
    <div className="container">
      <h1 className="title">AI Note Taker</h1>
      <h3 className="subtitle">Your personal AI assistant for video notes</h3>
      <input
        type="text"
        placeholder="Enter video URL..."
        value={videoUrl}
        onChange={(e) => setVideoUrl(e.target.value)}
        className="input"
      />
      <button
        onClick={handleSubmit}
        disabled={loading}
        className={`button ${loading ? "loading" : ""}`}
      >
        {loading ? "Processing..." : "Generate Notes"}
      </button>

      {notes && (
        <div className="notesContainer">
          <h2 className="notesTitle">Notes</h2>
          <textarea
            value={notes}
            readOnly
            rows={10}
            className="notesTextarea"
          />
        </div>
      )}
    </div>
  )
}

const styles = `
  body {
    width: 100%;
    height: 100%;
    margin: 0;
  }

  .container {
    width: 100vw;
    height: 100vh;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    box-sizing: border-box;
    padding: 20px;
  }
  .title {
    font-size: 24px;
    font-weight: bold;
    text-align: center;
    margin-bottom: 4px;
  }
  .subtitle {
    font-size: 18px;
    font-weight: 400;
    text-align: center;
    margin-top: 0;
    margin-bottom: 8px;
  }
  .input {
    width: 50%;
    padding: 10px;
    margin: 10px 0;
    font-size: 16px;
  }
  .button {
    padding: 10px 20px;
    font-size: 16px;
    background-color: #007bff;
    color: white;
    border: none;
    cursor: pointer;
    margin-bottom: 200px; 
  }
  .button:disabled {
    background-color: #ccc;
    cursor: not-allowed;
  }
  .notesContainer {
    margin-top: 20px;
    border: 1px solid #ddd;
    padding: 10px;
    width: 100%;
  }
  .notesTitle {
    font-size: 18px;
    font-weight: 600;
  }
  .notesTextarea {
    width: 100%;
    padding: 10px;
    margin-top: 10px;
    resize: none;
  }
`;


// Inject styles into the document
if (typeof document !== 'undefined') {
  const styleSheet = document.createElement("style");
  styleSheet.type = "text/css";
  styleSheet.innerText = styles;
  document.head.appendChild(styleSheet);
}


export default Dashboard;