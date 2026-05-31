import { useState } from 'react';
import Reel from './Reel';
import Leaderboard from './Leaderboard';
import { spin, getSession, resetSession } from '../api/slotApi';

const PAYOUT_TABLE = [
  { syms: '💎💎💎', pay: '×50' },
  { syms: '7️⃣7️⃣7️⃣', pay: '×20' },
  { syms: '🍀🍀🍀', pay: '×10' },
  { syms: '⭐⭐⭐', pay: '×8'  },
  { syms: '🍋🍋🍋', pay: '×4'  },
  { syms: '🍇🍇🍇', pay: '×3'  },
  { syms: '🍒🍒🍒', pay: '×2'  },
  { syms: 'Any 2 match', pay: '½ back' },
];

export default function SlotMachine() {
  const [playerName, setPlayerName] = useState('');
  const [nameInput, setNameInput] = useState('');
  const [session, setSession] = useState(null);
  const [reels, setReels] = useState(['🍒', '🍒', '🍒']);
  const [spinning, setSpinning] = useState(false);
  const [message, setMessage] = useState('');
  const [isWin, setIsWin] = useState(false);
  const [error, setError] = useState('');

  const handleJoin = async () => {
    if (!nameInput.trim()) return;
    try {
      const s = await getSession(nameInput.trim());
      setSession(s);
      setPlayerName(nameInput.trim());
      setMessage(`Welcome back, ${s.playerName}! You have ${s.coins} coins.`);
    } catch (e) {
      setError('Could not connect to server.');
    }
  };

  const handleSpin = async () => {
    if (spinning || !session) return;
    setSpinning(true);
    setMessage('');
    setError('');
    setIsWin(false);

    try {
      // Animate for 1.2s then show result
      setTimeout(async () => {
        try {
          const result = await spin(playerName);
          setReels(result.reels);
          setSession(prev => ({ ...prev, coins: result.remainingCoins, totalSpins: result.totalSpins, totalWins: result.totalWins }));
          setMessage(result.message);
          setIsWin(result.isWin);
        } catch (e) {
          setError(e.response?.data?.error || 'Spin failed.');
        } finally {
          setSpinning(false);
        }
      }, 1200);
    } catch (e) {
      setSpinning(false);
    }
  };

  const handleReset = async () => {
    try {
      const s = await resetSession(playerName);
      setSession(s);
      setMessage('Session reset! You have 100 coins.');
      setIsWin(false);
    } catch (e) {
      setError('Reset failed.');
    }
  };

  // ── Login Screen ─────────────────────────────────
  if (!playerName) {
    return (
      <div style={styles.bg}>
        <div style={styles.loginBox}>
          <h1 style={styles.title}>🎰 Slot Machine</h1>
          <p style={{ color: '#aaa', marginBottom: 16 }}>Enter your name to play</p>
          <input
            style={styles.input}
            placeholder="Your name..."
            value={nameInput}
            onChange={e => setNameInput(e.target.value)}
            onKeyDown={e => e.key === 'Enter' && handleJoin()}
          />
          <button style={styles.btnPrimary} onClick={handleJoin}>Play</button>
          {error && <p style={{ color: '#E24B4A', marginTop: 8 }}>{error}</p>}
        </div>
      </div>
    );
  }

  // ── Game Screen ───────────────────────────────────
  return (
    <div style={styles.bg}>
      <div style={styles.machine}>
        <h1 style={styles.title}>🎰 Slot Machine</h1>

        {/* Stats */}
        <div style={styles.statsRow}>
          <div style={styles.stat}><div style={styles.statLbl}>Player</div><div style={styles.statVal}>{playerName}</div></div>
          <div style={styles.stat}><div style={styles.statLbl}>Coins</div><div style={{ ...styles.statVal, color: '#EF9F27' }}>{session?.coins ?? 0}</div></div>
          <div style={styles.stat}><div style={styles.statLbl}>Spins</div><div style={styles.statVal}>{session?.totalSpins ?? 0}</div></div>
          <div style={styles.stat}><div style={styles.statLbl}>Wins</div><div style={styles.statVal}>{session?.totalWins ?? 0}</div></div>
        </div>

        {/* Reels */}
        <div style={styles.reelsRow}>
          {reels.map((sym, i) => <Reel key={i} symbol={sym} spinning={spinning} />)}
        </div>

        {/* Message */}
        <div style={{ ...styles.msg, color: isWin ? '#1D9E75' : '#aaa' }}>
          {message || 'Good luck! 🍀'}
        </div>
        {error && <div style={{ color: '#E24B4A', fontSize: 13 }}>{error}</div>}

        {/* Buttons */}
        <div style={styles.btnRow}>
          <button style={spinning || session?.coins < 10 ? styles.btnDisabled : styles.btnSpin}
            onClick={handleSpin} disabled={spinning || session?.coins < 10}>
            {spinning ? 'Spinning...' : 'SPIN  (-10)'}
          </button>
          <button style={styles.btnSecondary} onClick={handleReset}>Reset</button>
        </div>

        {/* Payout table */}
        <div style={styles.payoutBox}>
          <div style={{ color: '#EF9F27', fontWeight: 500, marginBottom: 8, fontSize: 13 }}>Payout Table</div>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 4 }}>
            {PAYOUT_TABLE.map((p, i) => (
              <div key={i} style={styles.payoutRow}>
                <span style={{ letterSpacing: 2 }}>{p.syms}</span>
                <span style={{ color: '#EF9F27', fontWeight: 500 }}>{p.pay}</span>
              </div>
            ))}
          </div>
        </div>

        <Leaderboard />
      </div>
    </div>
  );
}

const styles = {
  bg: { minHeight: '100vh', background: '#0a0a1a', display: 'flex', justifyContent: 'center', padding: '2rem 1rem' },
  loginBox: { background: '#16213e', borderRadius: 16, padding: 40, textAlign: 'center', width: '100%', maxWidth: 380, height: 'fit-content' },
  machine: { width: '100%', maxWidth: 480 },
  title: { color: '#EF9F27', fontSize: 28, fontWeight: 700, textAlign: 'center', marginBottom: 20 },
  statsRow: { display: 'grid', gridTemplateColumns: 'repeat(4,1fr)', gap: 8, marginBottom: 20 },
  stat: { background: '#16213e', borderRadius: 10, padding: '8px 6px', textAlign: 'center' },
  statLbl: { fontSize: 10, color: '#888', marginBottom: 2 },
  statVal: { fontSize: 16, fontWeight: 600, color: '#fff' },
  reelsRow: { display: 'flex', justifyContent: 'center', gap: 12, marginBottom: 16 },
  msg: { textAlign: 'center', fontSize: 15, fontWeight: 500, minHeight: 24, marginBottom: 16 },
  btnRow: { display: 'flex', gap: 10, justifyContent: 'center', marginBottom: 20 },
  btnSpin: { padding: '12px 32px', background: '#EF9F27', color: '#000', border: 'none', borderRadius: 10, fontWeight: 700, fontSize: 15, cursor: 'pointer' },
  btnDisabled: { padding: '12px 32px', background: '#444', color: '#888', border: 'none', borderRadius: 10, fontWeight: 700, fontSize: 15, cursor: 'not-allowed' },
  btnSecondary: { padding: '12px 20px', background: 'transparent', color: '#aaa', border: '1px solid #333', borderRadius: 10, cursor: 'pointer', fontSize: 14 },
  btnPrimary: { padding: '12px 32px', background: '#EF9F27', color: '#000', border: 'none', borderRadius: 10, fontWeight: 700, fontSize: 15, cursor: 'pointer', width: '100%', marginTop: 8 },
  input: { width: '100%', padding: '10px 14px', background: '#0f3460', border: '1px solid #1a4a80', borderRadius: 8, color: '#fff', fontSize: 14, marginBottom: 10 },
  payoutBox: { background: '#16213e', borderRadius: 12, padding: 14, marginBottom: 16, fontSize: 12 },
  payoutRow: { display: 'flex', justifyContent: 'space-between', background: '#0f3460', borderRadius: 6, padding: '4px 8px', color: '#ccc' },
};
