let backendHost;

const hostname = window && window.location && window.location.hostname;

if(hostname === 'localhost') {
    backendHost = 'http://localhost:8080';
} else {
    backendHost = 'https://rss-be-fantastic-kookaburra.cfapps.io';
}

export const API_ROOT = backendHost;