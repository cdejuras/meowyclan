import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8080/api/slot',
  headers: { 'Content-Type': 'application/json' },
});

export const spin = (playerName) =>
  API.post('/spin', { playerName }).then(r => r.data);

export const getSession = (playerName) =>
  API.get(`/session/${playerName}`).then(r => r.data);

export const getLeaderboard = () =>
  API.get('/leaderboard').then(r => r.data);

export const resetSession = (playerName) =>
  API.post(`/reset/${playerName}`).then(r => r.data);
