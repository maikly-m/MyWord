
#ifndef TEST_CRC_H
#define TEST_CRC_H
#include <cstdint>
extern unsigned int crc16(const unsigned char *data, int len);
extern uint16_t _crc_16(uint8_t *data, uint16_t len);

#endif //TEST_CRC_H
