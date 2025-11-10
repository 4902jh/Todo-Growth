// Todo 아이템 컴포넌트 (게임화 요소 포함)

import React, { useState } from 'react';
import './TodoItem.css';

const TodoItem = ({ todo, userId, onComplete, onFail }) => {
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState(null);

    const handleComplete = async () => {
        setLoading(true);
        setMessage(null);
        
        try {
            const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';
            const response = await fetch(`${API_BASE_URL}/game/users/${userId}/todos/${todo.id}/complete`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            
            const data = await response.json();
            
            if (data.success) {
                setMessage({
                    type: 'success',
                    text: data.leveledUp 
                        ? `🎉 레벨업! 경험치 +${data.experienceGained} 획득!`
                        : `✅ 완료! 경험치 +${data.experienceGained} 획득!`
                });
                
                // 부모 컴포넌트에 알림
                if (onComplete) {
                    onComplete(data);
                }
                
                // 3초 후 메시지 제거
                setTimeout(() => setMessage(null), 3000);
            }
        } catch (error) {
            setMessage({
                type: 'error',
                text: 'Todo 완료 처리 중 오류가 발생했습니다.'
            });
        } finally {
            setLoading(false);
        }
    };

    const handleFail = async () => {
        if (!window.confirm('정말로 이 Todo를 실패로 표시하시겠습니까? 경험치가 감소합니다.')) {
            return;
        }

        setLoading(true);
        setMessage(null);
        
        try {
            const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';
            const response = await fetch(`${API_BASE_URL}/game/users/${userId}/todos/${todo.id}/fail`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            
            const data = await response.json();
            
            if (data.success) {
                setMessage({
                    type: 'warning',
                    text: data.leveledUp
                        ? `⚠️ 레벨업! 경험치 ${data.experienceLost} 감소`
                        : `❌ 실패! 경험치 ${data.experienceLost} 감소`
                });
                
                if (onFail) {
                    onFail(data);
                }
                
                setTimeout(() => setMessage(null), 3000);
            }
        } catch (error) {
            setMessage({
                type: 'error',
                text: 'Todo 실패 처리 중 오류가 발생했습니다.'
            });
        } finally {
            setLoading(false);
        }
    };

    const isCompletedToday = todo.todayCompleted || false;

    return (
        <div className={`todo-item ${isCompletedToday ? 'completed' : ''}`}>
            <div className="todo-content">
                <h3>{todo.title}</h3>
                {todo.description && <p>{todo.description}</p>}
            </div>

            <div className="todo-actions">
                {!isCompletedToday ? (
                    <>
                        <button 
                            className="btn-complete"
                            onClick={handleComplete}
                            disabled={loading}
                        >
                            {loading ? '처리 중...' : '✅ 완료'}
                        </button>
                        <button 
                            className="btn-fail"
                            onClick={handleFail}
                            disabled={loading}
                        >
                            ❌ 실패
                        </button>
                    </>
                ) : (
                    <div className="completed-badge">
                        오늘 완료됨! 🎉
                    </div>
                )}
            </div>

            {message && (
                <div className={`message ${message.type}`}>
                    {message.text}
                </div>
            )}
        </div>
    );
};

export default TodoItem;

