import React from "react";
import { GoogleLogin } from "@react-oauth/google";
import { useNavigate } from "react-router-dom";

const LoginPage = () => {
  return (
    <div>
      <h1>Login Page</h1>
      <GoogleLogin
        onSuccess={(credentialResponse) => {
          console.log("Login Success:", credentialResponse);
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
