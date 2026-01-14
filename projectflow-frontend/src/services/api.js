import axios from "axios";

const API_URL = "http://localhost:8080/api";

export const getDashboard = async (studentId) => {
  try {
    const res = await axios.get(`${API_URL}/dashboard/${studentId}`);
    return res.data;
  } catch (err) {
    console.error("Erreur API:", err);
    return null;
  }
};
