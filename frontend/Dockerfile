### stage 1 ####

FROM node:20 AS deps
# Give Node 2GB memory
#ENV NODE_OPTIONS="--max-old-space-size=1024"
WORKDIR /app
COPY package*.json ./
RUN npm install

#####   stage 2 ###
FROM node:20 AS builder
WORKDIR /app
# 1. Copy the package files from the 'deps' stage
COPY --from=deps /app/node_modules ./node_modules
COPY . .
RUN npm run build

###  stage 3    ###
FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html/*
COPY --from=builder /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]  


