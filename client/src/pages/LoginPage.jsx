import React from "react";
import { GoogleLogin } from "@react-oauth/google";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode"; // ✅ Correct import

const LoginPage = () => {
  const navigate = useNavigate();

  return (
    <div>
      <h1>Login Page</h1>
      <GoogleLogin
        onSuccess={(credentialResponse) => {
          console.log("Login Success:", credentialResponse);
          // ✅ Correct usage of jwtDecode
          const decoded = jwtDecode(credentialResponse.credential);
          // Store credential and profile info in localStorage
          localStorage.setItem('google_credential', credentialResponse.credential);
          localStorage.setItem('google_profile', JSON.stringify({
            name: decoded.name,
            email: decoded.email,
            picture: decoded.picture
          }));
          navigate("/dashboard");
        }}
        onError={() => {
          console.log("Login Failed");
        }}
      />
      <p>Login with Google to continue</p>
    </div>
  );
};

export default LoginPage;
