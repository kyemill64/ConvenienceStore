from flask import Flask, jsonify, send_from_directory

app = Flask(__name__)
PORT = 8081
HTML_FILE = 'conven.html'

def get_convenience_stores():
    """편의점 데이터를 반환하는 함수"""
    return [
        {
            "name": "GS25 이촌역점",
            "brand": "GS25",
            "address": "서울특별시 용산구 이촌동 306-2",
            "lat": 37.5194,
            "lng": 126.9750,
            "phone": "02-749-1234",
            "hours": "24시간",
            "description": "이촌역 근처 GS25 편의점"
        },
        {
            "name": "GS25 한강점",
            "brand": "GS25",
            "address": "서울특별시 용산구 이촌동 302-5",
            "lat": 37.5178,
            "lng": 126.9762,
            "phone": "02-749-3456",
            "hours": "24시간",
            "description": "한강 근처 GS25 편의점"
        },
        {
            "name": "GS25 용산역점",
            "brand": "GS25",
            "address": "서울특별시 용산구 한강대로 23길 55",
            "lat": 37.5298,
            "lng": 126.9648,
            "phone": "02-749-1111",
            "hours": "24시간",
            "description": "용산역 근처 GS25 편의점"
        },
        {
            "name": "GS25 삼각지점",
            "brand": "GS25",
            "address": "서울특별시 용산구 한강대로 23길 55",
            "lat": 37.5347,
            "lng": 126.9727,
            "phone": "02-749-2222",
            "hours": "24시간",
            "description": "삼각지역 근처 GS25 편의점"
        },
        {
            "name": "GS25 남영점",
            "brand": "GS25",
            "address": "서울특별시 용산구 남영동 1-1",
            "lat": 37.5450,
            "lng": 126.9720,
            "phone": "02-749-3333",
            "hours": "24시간",
            "description": "남영동 GS25 편의점"
        },
        {
            "name": "CU 이촌점",
            "brand": "CU",
            "address": "서울특별시 용산구 이촌동 305-1",
            "lat": 37.5185,
            "lng": 126.9748,
            "phone": "02-749-5678",
            "hours": "24시간",
            "description": "이촌동 CU 편의점"
        },
        {
            "name": "CU 한강공원점",
            "brand": "CU",
            "address": "서울특별시 용산구 이촌동 301-8",
            "lat": 37.5172,
            "lng": 126.9768,
            "phone": "02-749-7890",
            "hours": "24시간",
            "description": "한강공원 근처 CU 편의점"
        },
        {
            "name": "CU 용산점",
            "brand": "CU",
            "address": "서울특별시 용산구 용산동 2가 1-1",
            "lat": 37.5310,
            "lng": 126.9650,
            "phone": "02-749-4444",
            "hours": "24시간",
            "description": "용산동 CU 편의점"
        },
        {
            "name": "CU 한남점",
            "brand": "CU",
            "address": "서울특별시 용산구 한남동 1-1",
            "lat": 37.5310,
            "lng": 127.0080,
            "phone": "02-749-5555",
            "hours": "24시간",
            "description": "한남동 CU 편의점"
        },
        {
            "name": "CU 서빙고점",
            "brand": "CU",
            "address": "서울특별시 용산구 서빙고동 1-1",
            "lat": 37.5190,
            "lng": 126.9880,
            "phone": "02-749-6666",
            "hours": "24시간",
            "description": "서빙고동 CU 편의점"
        },
        {
            "name": "세븐일레븐 이촌동점",
            "brand": "세븐일레븐",
            "address": "서울특별시 용산구 이촌동 307-3",
            "lat": 37.5201,
            "lng": 126.9755,
            "phone": "02-749-9012",
            "hours": "24시간",
            "description": "이촌동 세븐일레븐 편의점"
        },
        {
            "name": "세븐일레븐 용산점",
            "brand": "세븐일레븐",
            "address": "서울특별시 용산구 이촌동 308-2",
            "lat": 37.5210,
            "lng": 126.9745,
            "phone": "02-749-2468",
            "hours": "24시간",
            "description": "용산구 세븐일레븐 편의점"
        },
        {
            "name": "세븐일레븐 효창점",
            "brand": "세븐일레븐",
            "address": "서울특별시 용산구 효창동 1-1",
            "lat": 37.5400,
            "lng": 126.9620,
            "phone": "02-749-7777",
            "hours": "24시간",
            "description": "효창동 세븐일레븐 편의점"
        },
        {
            "name": "세븐일레븐 원효점",
            "brand": "세븐일레븐",
            "address": "서울특별시 용산구 원효로 1가 1-1",
            "lat": 37.5380,
            "lng": 126.9700,
            "phone": "02-749-8888",
            "hours": "24시간",
            "description": "원효로 세븐일레븐 편의점"
        },
        {
            "name": "이마트24 용산점",
            "brand": "이마트24",
            "address": "서울특별시 용산구 용산동 2가 1-1",
            "lat": 37.5320,
            "lng": 126.9660,
            "phone": "02-749-9999",
            "hours": "24시간",
            "description": "용산동 이마트24 편의점"
        },
        {
            "name": "이마트24 이촌점",
            "brand": "이마트24",
            "address": "서울특별시 용산구 이촌동 1-1",
            "lat": 37.5200,
            "lng": 126.9760,
            "phone": "02-749-0000",
            "hours": "24시간",
            "description": "이촌동 이마트24 편의점"
        }
    ]

def get_stores_by_brand(brand):
    """브랜드별 편의점을 필터링하는 함수"""
    all_stores = get_convenience_stores()
    return [store for store in all_stores if store["brand"] == brand]

@app.route('/')
@app.route('/index.html')
def index():
    """메인 페이지 라우트"""
    return send_from_directory('.', HTML_FILE)

@app.route('/<path:filename>')
def static_files(filename):
    """정적 파일 서빙 (API 경로 제외)"""
    # API 경로는 제외
    if filename.startswith('api/'):
        return "404 Not Found", 404
    
    try:
        return send_from_directory('.', filename)
    except FileNotFoundError:
        return "404 Not Found", 404

@app.route('/api/stores', methods=['GET'])
def api_stores():
    """모든 편의점 데이터 API"""
    try:
        stores = get_convenience_stores()
        response = jsonify(stores)
        response.headers['Access-Control-Allow-Origin'] = '*'
        response.headers['Content-Type'] = 'application/json; charset=utf-8'
        print(f"API 호출: /api/stores - {len(stores)}개 편의점 반환")
        return response
    except Exception as e:
        print(f"API 오류: {e}")
        return jsonify({"error": str(e)}), 500

@app.route('/api/stores/<brand>', methods=['GET'])
def api_stores_by_brand(brand):
    """브랜드별 편의점 데이터 API"""
    try:
        filtered_stores = get_stores_by_brand(brand)
        response = jsonify(filtered_stores)
        response.headers['Access-Control-Allow-Origin'] = '*'
        response.headers['Content-Type'] = 'application/json; charset=utf-8'
        print(f"API 호출: /api/stores/{brand} - {len(filtered_stores)}개 편의점 반환")
        return response
    except Exception as e:
        print(f"API 오류: {e}")
        return jsonify({"error": str(e)}), 500

@app.errorhandler(404)
def not_found(error):
    """404 에러 핸들러"""
    return "404 Not Found", 404

@app.errorhandler(500)
def internal_error(error):
    """500 에러 핸들러"""
    return "500 Internal Server Error", 500

if __name__ == '__main__':
    print(f"서버가 포트 {PORT}에서 시작되었습니다.")
    print(f"브라우저에서 http://localhost:{PORT} 를 열어보세요.")
    app.run(host='0.0.0.0', port=PORT, debug=True)