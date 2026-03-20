import { useState } from "react";
import { Container, Box, TextField, Button, Typography, Paper, Alert } from "@mui/material";
import { useAuth } from "../store/authStore";
import { useNavigate } from "react-router-dom";
import { updateUserApi } from "../api/authApi";

export default function EditProfilePage() {
  const { user, login } = useAuth();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    name: user?.name || '',
    password: '',
    confirmPassword: ''
  });

  const [error, setError] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const validate = () => {
    if (!form.name.trim()) return '이름을 입력해주세요.';
    
    if (form.password && form.password.length < 8) {
      return '비밀번호는 8자 이상이어야 합니다.';
    }

    if (form.password !== form.confirmPassword) {
      return '비밀번호가 일치하지 않습니다.';
    }

    return '';
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const validationError = validate();
    if (validationError) {
      setError(validationError);
      return;
    }

    try {
      const data = await updateUserApi({
        name: form.name,
        password: form.password || undefined // 빈 값이면 제외
      });

      login(data.token, {
        email: data.email,
        name: data.name,
        role: data.role
      });
      navigate('/', {
        state: { message: '회원정보가 수정되었습니다.', severity: 'success' }
      });
    } catch (err: any) {
      setError(err?.response?.data?.message || '수정 실패');
    }
  };

  return (
    <Container maxWidth="xs">
      <Box sx={{ mt: 8 }}>
        <Paper sx={{ p: 4 }}>
          <Typography variant="h6" mb={2}>
            회원정보 수정
          </Typography>

          {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

          <Box component="form" onSubmit={handleSubmit}>
            <TextField
              fullWidth
              label="이메일"
              value={user?.email}
              margin="normal"
              disabled
            />

            <TextField
              fullWidth
              label="이름"
              name="name"
              value={form.name}
              onChange={handleChange}
              margin="normal"
            />

            <TextField
              fullWidth
              label="새 비밀번호"
              name="password"
              type="password"
              value={form.password}
              onChange={handleChange}
              margin="normal"
            />

            <TextField
              fullWidth
              label="비밀번호 확인"
              name="confirmPassword"
              type="password"
              value={form.confirmPassword}
              onChange={handleChange}
              margin="normal"
            />

            <Button type="submit" fullWidth variant="contained" sx={{ mt: 2 }}>
              수정하기
            </Button>
          </Box>
        </Paper>
      </Box>
    </Container>
  );
}