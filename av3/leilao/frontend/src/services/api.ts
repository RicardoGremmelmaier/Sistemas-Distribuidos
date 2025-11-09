import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080",
    headers: {
        "Content-Type": "application/json",
    },
    withCredentials: false,
});

api.interceptors.request.use((config) => {
  console.log(`[API REQUEST] ${config.method?.toUpperCase()} ${config.url}`, config.data || "");
  return config;
});

api.interceptors.response.use(
  (response) => {
    console.log(`[API RESPONSE] ${response.status} ${response.config.url}`, response.data);
    return response;
  },
  (error) => {
    if (error.response) {
      console.error(
        `[API ERROR] ${error.response.status} ${error.config?.url}:`,
        error.response.data
      );
    } else {
      console.error("[API ERROR] Erro de rede ou servidor indisponível:", error.message);
    }
    return Promise.reject(error);
  }
);

export default api;