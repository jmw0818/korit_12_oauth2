import { Container, Box, Typography, Button, Paper, Chip, Avatar, Snackbar, Alert } from "@mui/material";
import LogoutIcon from '@mui/icons-material/Logout';
import PersonIcon from '@mui/icons-material/Person';
import { useAuth } from "../store/authStore";
import { useNavigate, useLocation } from "react-router-dom";
import { useEffect, useState } from "react";


export default function HomePage() {
  // 정의한 useAuth()를 사용해볼겁니다.
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const [snackbar, setSnackbar] = useState({
    open: false,
    message: '',
    severity: 'success' as 'success' | 'error' | 'info' | 'warning'
  });

  const handleLogout = () : void => {
    logout();
    setTimeout(() => {
        navigate('/login', { 
          replace: true,
          state: { message: '로그아웃 되었습니다.', severity: 'success'}
      });
    }, 0);
  }

  useEffect(() => {
    if (location.state?.message) {
      setSnackbar({
        open: true,
        message: location.state.message,
        severity: location.state.severity || 'success'
      });

      window.history.replaceState({}, document.title);
    }
  }, [location.state]);

  return (
    <>
      <Container maxWidth='sm'>
        <Box sx={{mt: 8}}>
          <Paper elevation={3} sx={{p: 4, textAlign: 'center'}}>
            <Avatar sx={{width: 80, height: 80, mx: 'auto', mb: 2, bgcolor: 'primary.main'}}>
              <PersonIcon fontSize="large"/>
            </Avatar>
            {/* user?.name : user가 UserInfo 자료형을 따르는 객체거나 null이라는 뜻인데, null이면 name이 없다. 그때 오류가 발생하지 않고 undefined를 리턴해주게 된다. */}
            <Typography variant="h5" fontWeight='bold' gutterBottom>
              환영합니다. {user?.name} 님 ! 💀
            </Typography>
            <Box sx={{textAlign: 'left', bgcolor: 'gray.50', p: 2, borderRadius: 1, mb: 3}}>
              <Typography variant="body2" color='text.secondary'>이메일</Typography>
              <Typography variant="body1" fontWeight='medium'>{user.email}</Typography>
              <Typography variant="body2" color='text.secondary'>권한</Typography>
              <Typography variant="body1" fontWeight='medium'>{user.role}</Typography>
            </Box>
            <Button
              variant="contained"
              sx={{ mb: 2 }}
              onClick={() => navigate('/profile/verify')}
              fullWidth
            >
              회원정보 수정
            </Button>
            <Button
              variant="outlined"
              color="error"
              startIcon={<LogoutIcon />}
              onClick={handleLogout}
              fullWidth
            >
              logout
            </Button>
          </Paper>
        </Box>
      </Container>
      <Snackbar
        open={snackbar.open}
        autoHideDuration={3000}
        onClose={() => setSnackbar(prev => ({ ...prev, open: false }))}
        anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
      >
        <Alert severity={snackbar.severity} sx={{ width: '100%' }}>
          {snackbar.message}
        </Alert>
      </Snackbar>
    </>
  );
}