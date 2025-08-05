import { useState } from 'react';
import './App.css';

function App() {
  const [texto, setTexto] = useState('');
  const [jobId, setJobId] = useState(null);
  const [resultado, setResultado] = useState('');
  const [estado, setEstado] = useState('');
  const [esperando, setEsperando] = useState(false);

  const enviarTexto = async () => {
    setEsperando(true);
    try {
      const response = await fetch('https://analitycore-python.onrender.com/submit',{
      //const response = await fetch('http://localhost:8000/submit', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ texto }),
      });

      const data = await response.json();
      setJobId(data.jobId); // ✅ Cambiado de data.id a data.jobId
      setEstado('PENDIENTE');
      setResultado('');
    } catch (error) {
      console.error('Error al enviar texto:', error);
      alert('No se pudo enviar el texto. Asegúrate de que el backend está activo.');
    }
  };

  const consultarResultado = async () => {
    try {
      const response = await fetch(`https://analitycore-python.onrender.com/status/${jobId}`);
      //const response = await fetch(`http://localhost:8000/status/${jobId}`);
      const data = await response.json();
      setEstado(data.estado);
      if (data.estado === 'COMPLETADO') {
        setResultado(data.resultado);
        setEsperando(false);
      }
    } catch (error) {
      console.error('Error al consultar estado:', error);
      alert('No se pudo consultar el estado.');
    }
  };

  return (
    <div className="App">
      <h1>AnalytiCore</h1>

      <textarea
        rows="4"
        cols="50"
        placeholder="Ingresa tu texto aquí"
        value={texto}
        onChange={(e) => setTexto(e.target.value)}
      />

      <br />

      <button onClick={enviarTexto} disabled={esperando || !texto.trim()}>
        Enviar Texto
      </button>

      {jobId && (
        <>
          <p><strong>Estado:</strong> {estado}</p>
          {estado !== 'COMPLETADO' && (
            <button onClick={consultarResultado}>Consultar Estado</button>
          )}
          {resultado && (
            <div>
              <h3>Resultado</h3>
              <p>{resultado}</p>
            </div>
          )}
        </>
      )}
    </div>
  );
}

export default App;
