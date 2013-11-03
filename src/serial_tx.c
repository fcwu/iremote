#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <errno.h>
#include <termios.h>
#include <sys/types.h>
#include <signal.h>

int
set_interface_attribs (int fd, int speed, int parity)
{
    struct termios tty;
    memset (&tty, 0, sizeof tty);
    if (tcgetattr (fd, &tty) != 0)
    {
        printf ("error %d from tcgetattr", errno);
        return -1;
    }

    cfsetospeed (&tty, speed);
    cfsetispeed (&tty, speed);

    tty.c_cflag = (tty.c_cflag & ~CSIZE) | CS8;     // 8-bit chars
    // disable IGNBRK for mismatched speed tests; otherwise receive break
    // as \000 chars
    tty.c_iflag |= IGNBRK;         // ignore break signal
    tty.c_iflag &= ~IGNPAR;         // ignore break signal
    tty.c_lflag = 0;                // no signaling chars, no echo,
    // no canonical processing
    tty.c_oflag = 0;                // no remapping, no delays
    tty.c_cc[VMIN]  = 0;            // read doesn't block
    tty.c_cc[VTIME] = 5;            // 0.5 seconds read timeout

    tty.c_iflag &= ~(IXON | IXOFF | IXANY); // shut off xon/xoff ctrl

    tty.c_cflag |= (CLOCAL | CREAD);// ignore modem controls,
    // enable reading
    tty.c_cflag &= ~(PARENB | PARODD);      // shut off parity
    tty.c_cflag |= parity;
    tty.c_cflag &= ~CSTOPB;
    tty.c_cflag &= ~CRTSCTS;    // hardware flow control
    tty.c_cflag &= ~PARENB;     //不允許同位元檢查
    tty.c_cflag &= ~CSTOPB;     //不是2停止位元
    tty.c_cflag |= CS8;             //8 bits
    tty.c_cflag |= speed;             //8 bits
    tty.c_cflag |= HUPCL;             //8 bits
    tcflush(fd, TCIFLUSH);
    if (tcsetattr (fd, TCSANOW, &tty) != 0) {
        printf("error %d from tcsetattr", errno);
        return -1;
    }
    return 0;
}

void
set_blocking (int fd, int should_block) {
    struct termios tty;
    memset (&tty, 0, sizeof tty);
    if (tcgetattr (fd, &tty) != 0) {
        printf ("error %d from tggetattr", errno);
        return;
    }

    tty.c_cc[VMIN]  = should_block ? 1 : 0;
    tty.c_cc[VTIME] = 5;            // 0.5 seconds read timeout

    if (tcsetattr (fd, TCSANOW, &tty) != 0)
        printf("error %d setting term attributes", errno);
}

int open_and_set(char * portname) {
    //int fd = open (portname, O_RDWR | O_NOCTTY | O_SYNC);
    int fd = open (portname, O_RDWR | O_NOCTTY);
    if (fd < 0) {
        printf("Failed to open %s: (%d) %s\n", portname, errno, strerror(errno));
        return -1;
    }
    if (set_interface_attribs (fd, B9600, 0) < 0) {
        return -2;
    }
    set_blocking (fd, 0);                // set no blocking
    return fd;
}


int main(int argc, char *argv[]) {
    int i;
    pid_t pid;
    char *portname = "/dev/ttyACM0";
    //int pipefd[2];
    if (argc <= 1) {
        printf("Wait to read...\n");;
        char buf [100] = {0};
        int fd = open_and_set(portname);
        while (read (fd, buf, sizeof buf) >= 0) {
        }
        return 0;
    }
    //if (pipe(pipefd) < 0) {
    //    perror("pipe");
    //    exit(EXIT_FAILURE);
    //}
    //pid = fork();
    //if (pid == 0) {
    //    int fd = open_and_set(portname);
    //    char buf [100] = {0};
    //    printf("C: write\n");
    //    for (i = 1; i < argc; ++i) {
    //        write (fd, argv[i], 1);
    //    }
    //    if (set_interface_attribs (fd, B9600, 0) < 0) {
    //        return -2;
    //    }
    //    set_blocking (fd, 1);                // set no blocking
    //    write(pipefd[1], buf, sizeof buf);
    //    printf("C: wait read\n");
    //    read (fd, buf, sizeof buf);  // read up to 100 characters if ready to read
    //    printf("C: readed\n");
    //    printf("%s\n", buf);
    //    printf("C: bye\n");
    //    close(fd);
    //} else if (pid > 0) {
    //    char buf[32] = {0};
    //    printf("P: wait read\n");
    //    read(pipefd[0], buf, sizeof buf);
    //    printf("P: readed\n");
    //    int fd = open_and_set(portname);
    //    for (i = 1; i < argc; ++i) {
    //        write (fd, argv[i], 1);
    //    }
    //    set_blocking (fd, 0);                // set no blocking
    //    printf("P: wait close\n");
    //    printf("P: wait read\n");
    //    //read (fd, buf, sizeof buf);  // read up to 100 characters if ready to read
    //    printf("P: readed\n");
    //    sleep(10);
    //    close(fd);
    //} else {
    //    perror("fork");
    //    exit(EXIT_FAILURE);
    //}
    int fd = open_and_set(portname);
    for (i = 1; i < argc; ++i) {
        write (fd, argv[i], 1);
    }
    return 0;
}
