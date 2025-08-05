# Imagen base oficial de Python
FROM python:3.10-slim

# Establecer directorio de trabajo
WORKDIR /app

# Copiar el contenido de la carpeta al contenedor
COPY . .

# Instalar dependencias
RUN pip install --no-cache-dir -r requirements.txt

# Exponer el puerto usado por Uvicorn
EXPOSE 8000

# Comando para producción
CMD ["uvicorn", "app:app", "--host", "0.0.0.0", "--port", "8000"]
