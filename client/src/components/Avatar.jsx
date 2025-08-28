import React from "react";

const Avatar = ({ imageUrl, onLogout, name }) => {
  return (
    <div style={{ textAlign: "center" }}>
      <button
        onClick={onLogout}
        style={{
          border: "none",
          background: "transparent",
          padding: 0,
          cursor: "pointer",
          outline: "none",
        }}
        title="Logout"
      >
        <img
          src={imageUrl}
          alt="Profile"
          onError={(e) => {
            e.target.src = "https://www.gravatar.com/avatar/?d=mp";
          }}
          style={{
            width: 40,
            height: 40,
            borderRadius: "50%",
            objectFit: "cover",
            border: "2px solid #007bff",
            boxShadow: "0 1px 4px rgba(0,0,0,0.1)",
          }}
        />
      </button>
      {name && (
        <div
          style={{
            fontSize: "0.75rem",
            marginTop: 4,
            color: "white",
            textAlign: "center",
            maxWidth: 80,
            wordWrap: "break-word",
          }}
        >
          {name}
        </div>
      )}
    </div>
  );
};

export default Avatar;
