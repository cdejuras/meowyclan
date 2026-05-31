import { useEffect, useState } from 'react';

const SYMBOLS = ['🍒','🍇','🍋','⭐','🍀','7️⃣','💎'];

/**
 * Reel component — animates spinning before showing final symbol.
 */
export default function Reel({ symbol, spinning }) {
  const [display, setDisplay] = useState(symbol);

  useEffect(() => {
    if (!spinning) { setDisplay(symbol); return; }
    const interval = setInterval(() => {
      setDisplay(SYMBOLS[Math.floor(Math.random() * SYMBOLS.length)]);
    }, 80);
    return () => clearInterval(interval);
  }, [spinning, symbol]);

  return (
    <div style={{
      width: 90, height: 90,
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      fontSize: 48,
      background: spinning ? '#1a1a2e' : '#16213e',
      border: `2px solid ${spinning ? '#EF9F27' : '#0f3460'}`,
      borderRadius: 12,
      transition: 'border-color .2s',
      boxShadow: spinning ? '0 0 12px #EF9F27' : 'none',
    }}>
      {display}
    </div>
  );
}
