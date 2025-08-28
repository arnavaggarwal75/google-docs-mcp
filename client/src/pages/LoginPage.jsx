import React from "react";
import { GoogleLogin } from "@react-oauth/google";
import { useNavigate } from "react-router-dom";
import {jwtDecode } from "jwt-decode";

const LoginPage = () => {

  const navigate = useNavigate();

  return (
    <div>
      <h1>Login Page</h1>
      <GoogleLogin
        onSuccess={(credentialResponse) => {
          console.log("Login Success:", credentialResponse);
          const user = jwtDecode(credentialResponse.credential);
          console.log("Decoded User:", user);
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
