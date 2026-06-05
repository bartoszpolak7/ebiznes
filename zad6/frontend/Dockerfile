FROM node:26-alpine3.22

WORKDIR /app

COPY package*.json ./

RUN npm ci --ignore-scripts

COPY tsconfig.json tsconfig.app.json tsconfig.node.json ./

COPY src ./src

COPY index.html ./

COPY vite.config.ts ./

RUN adduser -D appuser && chown -R appuser /app

USER appuser

EXPOSE 5173

CMD ["npm", "run", "dev", "--", "--host"]