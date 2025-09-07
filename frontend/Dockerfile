# ---

# Frontend Dockerfile - 3-stage build

# ---

# Stage 1: Dependencies

# Base Image
FROM node:18-alpine AS deps

# Work Dir
WORKDIR /app

# Copy
COPY package*.json ./

# Run
RUN npm ci --only=production

# ---

# Stage 2: Build

# Base Image
FROM node:18-alpine AS builder

# Work Dir
WORKDIR /app

# Copy
COPY --from=deps /app/node_modules ./node_modules
COPY . .

#Run
RUN npm run build

# ---

# Stage 3: Runtime

# Base Image
FROM nginx:alpine

# Copy
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=builder /app/build /usr/share/nginx/html

# Install curl for health checks
RUN apk add --no-cache curl

# Fix permissions (nginx user already exists in nginx:alpine)
RUN chown -R nginx:nginx /usr/share/nginx/html && \
    chown -R nginx:nginx /var/cache/nginx && \
    chown -R nginx:nginx /var/log/nginx && \
    chown -R nginx:nginx /etc/nginx/conf.d && \
    touch /var/run/nginx.pid && \
    chown -R nginx:nginx /var/run/nginx.pid

# User
USER nginx

# Expose Port
EXPOSE 3000

# Final Run
CMD ["nginx", "-g", "daemon off;"]

# ---
