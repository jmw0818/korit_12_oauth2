import { useState } from "react";
import { Container, Box, TextField, Button, Typography, Paper, Alert } from "@mui/material";
import { useNavigate } from "react-router-dom";
import api from "../api/authApi";

export default function VerifyPasswordPage() {
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      await api.post('/api/auth/verify-password', { password });
      navigate('/profile/edit');
    } catch (err: any) {
      setError(err?.response?.data?.message || '비밀번호가 일치하지 않습니다.');
    }
  };

  return (
    <Container maxWidth="xs">
      <Box sx={{ mt: 8 }}>
        <Paper sx={{ p: 4 }}>
          <Typography variant="h6" mb={2}>
            비밀번호 확인
          </Typography>

          {error && <Alert severity="error">{error}</Alert>}

          <Box component="form" onSubmit={handleSubmit}>
            <TextField
              fullWidth
              label="비밀번호"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              margin="normal"
            />

            <Button type="submit" fullWidth variant="contained" sx={{ mt: 2 }}>
              확인
            </Button>
          </Box>
        </Paper>
      </Box>
    </Container>
  );
}