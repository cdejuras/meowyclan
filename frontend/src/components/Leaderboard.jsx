import { useEffect, useState } from 'react';
import { getLeaderboard } from '../api/slotApi';

export default function Leaderboard() {
  const [board, setBoard] = useState([]);

  useEffect(() => {
    getLeaderboard().then(setBoard).catch(console.error);
  }, []);

  return (
    <div style={{ marginTop: 24, background: '#16213e', borderRadius: 12, padding: 16 }}>
      <h3 style={{ color: '#EF9F27', marginBottom: 12, textAlign: 'center' }}>🏆 Leaderboard</h3>
      <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 13 }}>
        <thead>
          <tr style={{ color: '#aaa', borderBottom: '1px solid #0f3460' }}>
            <th style={th}>#</th>
            <th style={th}>Player</th>
            <th style={th}>Winnings</th>
            <th style={th}>Wins</th>
            <th style={th}>Spins</th>
          </tr>
        </thead>
        <tbody>
          {board.map((p, i) => (
            <tr key={p.id} style={{ color: i === 0 ? '#EF9F27' : '#ccc', borderBottom: '1px solid #0f3460' }}>
              <td style={td}>{i + 1}</td>
              <td style={td}>{p.playerName}</td>
              <td style={td}>{p.totalWinnings}</td>
              <td style={td}>{p.totalWins}</td>
              <td style={td}>{p.totalSpins}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

const th = { padding: '6px 8px', textAlign: 'left' };
const td = { padding: '6px 8px' };
