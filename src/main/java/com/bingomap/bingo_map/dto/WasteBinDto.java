package com.bingomap.bingo_map.dto;

/**
 * [공공 쓰레기통 정보 DTO(Data Transfer Object) 클래스]
 * - DTO란?: "데이터 전송 객체"라는 뜻으로, DB 테이블 원본(엔티티) 데이터를 직접 노출하지 않고
 *            지도 화면(HTML/JS)에 꼭 필요한 핵심 좌표와 명칭만 안전하게 담아 나르는 '배달용 선물 상자'입니다.
 * - 지도 화면(map.html / map.js)에서 지도 핀(마커)을 찍거나 사이드바 목록을 표시할 때 이 상자를 사용합니다.
 */
public class WasteBinDto {

    // [쓰레기통 고유 식별 번호] 각 쓰레기통을 구분하는 고유 ID 번호표입니다.
    private Long id;

    // [위도 좌표 (Latitude)] 지도에서 위아래(남북) 세로 위치를 찍어주는 소수점 좌표입니다. (예: 34.668729)
    private double lat;

    // [경도 좌표 (Longitude)] 지도에서 좌우(동서) 가로 위치를 찍어주는 소수점 좌표입니다. (예: 135.501294)
    private double lon;

    // [쓰레기통 명칭] 지도 핀을 눌렀을 때 말풍선(팝업)에 뜰 장소 이름입니다. (예: '도톤보리 다리 앞 수거함')
    private String name;

    // [쓰레기통 분류 (카테고리)] 지도 마커의 색상(초록/파랑/주황)을 결정하는 구분값입니다.
    // - general : 일반 쓰레기통 (초록색 핀)
    // - recycle : 재활용 분리수거함 (파란색 핀)
    // - can     : 캔/병 전용 수거함 (주황색 핀)
    private String category;

    // [설치 위치 주소] 사용자가 찾아갈 수 있도록 안내하는 실제 도로명/지번 주소 또는 상세 위치 설명입니다.
    private String address;

    /**
     * [전체 파라미터 생성자 (Constructor)]
     * 서비스(Service)나 오라클 DB에서 쓰레기통 데이터를 가져온 뒤,
     * 위도, 경도, 이름 등을 이 생성자의 재료로 쏙 넣어주면 깔끔하게 포장된 배달 상자(DTO)가 완성됩니다.
     */
    public WasteBinDto(Long id, double lat, double lon, String name, String category, String address) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.category = category;
        this.address = address;
    }

    // =================================================================
    // [게터(Getter) 메서드 구역]
    // 밖(컨트롤러, JSON 변환기, 화면 스크립트)에서 상자 속 데이터들을 안전하게 꺼내볼 수 있게 열어주는 통로입니다.
    // =================================================================

    public Long getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getAddress() {
        return address;
    }
}