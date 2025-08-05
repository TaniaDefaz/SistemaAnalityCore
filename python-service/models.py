from sqlalchemy import Column, String, Text
from database import Base

class Job(Base):
    __tablename__ = "job"

    id = Column(String, primary_key=True, index=True)
    texto = Column(Text, nullable=False)
    estado = Column(String, default="PENDIENTE")
    resultado = Column(Text, nullable=True)
