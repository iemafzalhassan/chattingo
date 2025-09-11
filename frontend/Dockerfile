# Stage 1: Nodejs build environment
FROM node:20-alpine AS Main
WORKDIR /app
COPY package*.json ./
RUN npm install

# Stage 2: Build React app
FROM Main AS build
COPY . .
RUN npm run build

# Stage 3:  Nginx runtine server
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
