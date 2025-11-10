// 캐릭터 상태 표시 컴포넌트

import React, { useEffect, useState, useCallback } from 'react';
import './CharacterDisplay.css';

const CharacterDisplay = ({ userId }) => {
    const [character, setCharacter] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchCharacterStatus = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);
            const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';
            const response = await fetch(`${API_BASE_URL}/game/users/${userId}/character`);
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            const data = await response.json();
            if (data.success) {
                setCharacter(data.data);
            } else {
                setError(data.error || '캐릭터 정보를 불러올 수 없습니다.');
            }
        } catch (error) {
            console.error('캐릭터 정보 로드 실패:', error);
            setError(`연결 실패: ${error.message}. 백엔드 서버가 실행 중인지 확인하세요.`);
        } finally {
            setLoading(false);
        }
    }, [userId]);

    useEffect(() => {
        fetchCharacterStatus();
    }, [fetchCharacterStatus]);

    if (loading) {
        return (
            <div className="character-display">
                <div style={{ textAlign: 'center', padding: '20px', color: 'white' }}>
                    로딩 중...
                </div>
            </div>
        );
    }

    if (error || !character) {
        return (
            <div className="character-display">
                <div style={{ textAlign: 'center', padding: '20px', color: 'white' }}>
                    <p style={{ marginBottom: '10px' }}>⚠️ 캐릭터 정보를 불러올 수 없습니다</p>
                    <p style={{ fontSize: '14px', opacity: 0.9 }}>{error || '캐릭터가 존재하지 않습니다.'}</p>
                    <button 
                        onClick={fetchCharacterStatus}
                        style={{
                            marginTop: '10px',
                            padding: '8px 16px',
                            background: 'rgba(255, 255, 255, 0.2)',
                            border: '1px solid white',
                            borderRadius: '8px',
                            color: 'white',
                            cursor: 'pointer'
                        }}
                    >
                        다시 시도
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className="character-display">
            <div className="character-header">
                <h2>내 캐릭터</h2>
                <div className="level-badge">Lv.{character.level}</div>
            </div>

            {/* 캐릭터 아바타 (레벨에 따라 변경 가능) */}
            <div className="character-avatar">
                <div className={`avatar-level-${Math.min(character.level, 10)}`}>
                    🎮
                </div>
            </div>

            {/* 경험치 바 */}
            <div className="exp-bar-container">
                <div className="exp-bar-label">
                    경험치: {character.experience} / {character.requiredExperience}
                </div>
                <div className="exp-bar">
                    <div 
                        className="exp-bar-fill"
                        style={{ width: `${character.experienceProgress}%` }}
                    />
                </div>
                <div className="exp-percentage">
                    {character.experienceProgress.toFixed(1)}%
                </div>
            </div>

            {/* 레벨업 안내 */}
            <div className="level-up-info">
                <p>경험치가 {character.requiredExperience} 이상이면 레벨업합니다!</p>
            </div>
        </div>
    );
};

export default CharacterDisplay;
