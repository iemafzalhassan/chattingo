// API Base URL configuration

// Derive API base URL
// 1) Prefer explicit env (REACT_APP_API_URL)
// 2) If running on a non-localhost host, use current hostname with backend port 8080
// 3) Fallback to your VPS IP for production or localhost for dev
const deriveDefaultApiBaseUrl = () => {
  try {
    const { protocol, hostname } = window.location;
    const isLocalhost = hostname === "localhost" || hostname === "127.0.0.1";
    if (!isLocalhost && hostname) {
      const scheme = protocol === "https:" ? "https:" : "http:";
      return `${scheme}//${hostname}:8080`;
    }
  } catch (_) {
    // window not available (e.g., tests) – ignore and use fallback
  }
  // VPS fallback (your server IP + port)
  return "http://72.60.111.65:8081";
};

export const BASE_API_URL =
  process.env.REACT_APP_API_URL && process.env.REACT_APP_API_URL.trim() !== ""
    ? process.env.REACT_APP_API_URL
    : deriveDefaultApiBaseUrl();
