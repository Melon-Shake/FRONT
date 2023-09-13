
<script>
    let responseData = null;

    async function fetchData() {
        try {
            const response = await fetch('/api/data'); // 서버 API 엔드포인트
            if (!response.ok) {
                throw new Error('서버로부터 데이터를 가져오지 못했습니다.');
            }
            const data = await response.json(); // JSON으로 파싱
            responseData = data; // responseData에 파싱된 데이터 할당
        } catch (error) {
            console.error('데이터 가져오기 오류:', error);
        }
    }
</script>

<button on:click={fetchData}>데이터 가져오기</button>

{#if responseData}
    <p>서버로부터 받은 데이터: {responseData}</p>
{/if}