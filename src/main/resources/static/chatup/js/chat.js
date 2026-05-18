function getCsrfToken() {
    const value = `; ${document.cookie}`;
    const parts = value.split('; XSRF-TOKEN=');
    if (parts.length === 2) return parts.pop().split(';').shift();
    return '';
}

async function sendChatMessage(messageContent) {
    const token = getCsrfToken();

    try {
        const response = await fetch('/api/v1/chat', {
            method: 'POST',
            credentials: 'same-origin',
            headers: {
                'Content-Type': 'application/json',
                'X-XSRF-TOKEN': token
            },
            body: JSON.stringify({ message: messageContent })
        });
        if (response.status === 403) {
            console.error("CSRF-fel eller oinloggad");
            return;
        }
        const data = await response.json();
        console.log("Svar från chat: ", data.length);
    }catch( error ){
        console.error("Nätverksfel: ", error)
    }
}
