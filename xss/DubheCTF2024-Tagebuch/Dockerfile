FROM node:slim

ENV PNPM_HOME="/pnpm"
ENV PATH="$PNPM_HOME:$PATH"
RUN corepack enable

RUN apt-get update && \
    apt-get install -y --no-install-recommends chromium && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

RUN useradd -m app && \
    mkdir -p /app /pnpm && \
    chown -R app:app /app /pnpm

USER app
COPY --chown=app:app . /app
WORKDIR /app
ENV PUPPETEER_EXECUTABLE_PATH=/usr/bin/chromium
ENV PUPPETEER_SKIP_CHROMIUM_DOWNLOAD=true
ENV PUPPETEER_DISABLE_HEADLESS_WARNING=true
RUN pnpm install --frozen-lockfile && \
    pnpm store prune

ENV NODE_ENV=production
CMD [ "timeout", "--signal=SIGKILL", "15m", "pnpm", "start" ]
