from fastapi import FastAPI, Depends, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from sqlalchemy.orm import Session
import uuid
import httpx  # para hacer la llamada al servicio Java

from database import Base, engine, SessionLocal
from models import Job

app = FastAPI()

# Configuración CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # para desarrollo
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Crear tablas si no existen
Base.metadata.create_all(bind=engine)

class TextRequest(BaseModel):
    texto: str

# Dependencia de base de datos
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

# Endpoint para recibir texto
@app.post("/submit")
def submit_text(data: TextRequest, db: Session = Depends(get_db)):
    job_id = str(uuid.uuid4())

    # 1. Guardar trabajo como PENDIENTE
    nuevo_job = Job(id=job_id, texto=data.texto, estado="PENDIENTE", resultado=None)
    db.add(nuevo_job)
    db.commit()
    db.refresh(nuevo_job)
    print(f" Trabajo {job_id} guardado en PostgreSQL con estado: PENDIENTE")

    # 2. Llamar al microservicio Java para procesar (ya no se crea)
    try:
        java_procesar_url = f"https://analitycore-java.onrender.com/api/analyze/{job_id}"

        #java_procesar_url = f"http://servicio-java:8080/api/analyze/{job_id}"

        print(" Iniciando análisis en el microservicio Java...")
        procesar_response = httpx.post(java_procesar_url, timeout=15)
        procesar_response.raise_for_status()
        print(" Análisis completado exitosamente por el microservicio Java.")

    except Exception as e:
        print(" Error al llamar al servicio Java:", e)

    return {"jobId": job_id, "estado": "PENDIENTE"}

# Endpoint para consultar estado del trabajo
@app.get("/status/{job_id}")
def get_status(job_id: str, db: Session = Depends(get_db)):
    job = db.query(Job).filter(Job.id == job_id).first()
    if not job:
        raise HTTPException(status_code=404, detail="Job no encontrado")

    print(f" Consulta del estado del trabajo {job_id}: {job.estado}")
    return {
        "texto": job.texto,
        "estado": job.estado,
        "resultado": job.resultado
    }
