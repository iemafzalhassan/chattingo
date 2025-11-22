### stage 1: build react app ###
FROM node:20 AS builder
WORKDIR /app

# Copy package files and install all deps (including devDependencies like react-scripts)
COPY package*.json ./
RUN npm install --include=dev

# Copy the rest of the app source and build it
COPY . .
RUN npm run build

### stage 2: serve with nginx ###
FROM nginx:alpine

# Remove default nginx html
RUN rm -rf /usr/share/nginx/html/*

# Copy built React app from builder stage
COPY --from=builder /app/build /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
