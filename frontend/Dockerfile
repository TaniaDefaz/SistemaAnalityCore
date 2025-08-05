# Imagen base de Nginx
FROM nginx:alpine

# Copia el archivo de configuración personalizado
COPY nginx.conf /etc/nginx/nginx.conf

# Copia los archivos estáticos del build de React al directorio raíz de Nginx
COPY dist /usr/share/nginx/html

# Exponer el puerto
EXPOSE 80

# Comando por defecto
CMD ["nginx", "-g", "daemon off;"]
