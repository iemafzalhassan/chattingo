FROM node:18-alpine AS deps
WORKDIR /app
COPY package.json package-lock.json* ./
RUN npm install

FROM node:18-alpine AS build
WORKDIR /app
COPY --from=deps /app/node_modules /app/node_modules
COPY . .
ARG REACT_APP_API_URL=/api
ARG REACT_APP_WS_URL=/ws
ARG REACT_APP_ENV=production
ARG REACT_APP_DEBUG=false
ENV REACT_APP_API_URL=${REACT_APP_API_URL}
ENV REACT_APP_WS_URL=${REACT_APP_WS_URL}
ENV REACT_APP_ENV=${REACT_APP_ENV}
ENV REACT_APP_DEBUG=${REACT_APP_DEBUG}
RUN npm run build

FROM nginx:alpine AS runtime
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]


