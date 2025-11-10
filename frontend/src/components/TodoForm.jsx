// Todo 생성 폼 컴포넌트

import React, { useState } from 'react';
import './TodoForm.css';

const TodoForm = ({ userId, onTodoCreated, onCancel }) => {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!title.trim()) {
            setError('제목을 입력해주세요.');
            return;
        }

        setLoading(true);
        setError(null);

        try {
            const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';
            const response = await fetch(`${API_BASE_URL}/todos/users/${userId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    title: title.trim(),
                    description: description.trim()
                })
            });

            const data = await response.json();

            if (data.success) {
                setTitle('');
                setDescription('');
                if (onTodoCreated) {
                    onTodoCreated();
                }
            } else {
                setError(data.error || 'Todo 생성에 실패했습니다.');
            }
        } catch (error) {
            setError('Todo 생성 중 오류가 발생했습니다.');
            console.error('Todo 생성 실패:', error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="todo-form-container">
            <form onSubmit={handleSubmit} className="todo-form">
                <div className="form-group">
                    <label htmlFor="title">제목 *</label>
                    <input
                        type="text"
                        id="title"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        placeholder="Todo 제목을 입력하세요"
                        disabled={loading}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="description">설명</label>
                    <textarea
                        id="description"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        placeholder="Todo 설명을 입력하세요 (선택사항)"
                        rows="4"
                        disabled={loading}
                    />
                </div>

                {error && (
                    <div className="error-message">
                        {error}
                    </div>
                )}

                <div className="form-actions">
                    <button
                        type="button"
                        className="btn-cancel"
                        onClick={onCancel}
                        disabled={loading}
                    >
                        취소
                    </button>
                    <button
                        type="submit"
                        className="btn-submit"
                        disabled={loading || !title.trim()}
                    >
                        {loading ? '생성 중...' : '생성'}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default TodoForm;

