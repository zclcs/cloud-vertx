redis.replicate_commands()
local max = tonumber(ARGV[1])
local ttl = tonumber(ARGV[2])
local time = redis.call("TIME")
local now = tonumber(time[1]) * 1000 + math.floor(tonumber(time[2]) / 1000)
local expired = now - ttl
redis.call('zremrangebyscore', KEYS[1], 0, expired)
local currentLimit = tonumber(redis.call('zcard', KEYS[1]))
local nextLimit = currentLimit + 1
if nextLimit > max then
    return 0;
else
    redis.call("zadd", KEYS[1], now, now)
    redis.call("pexpire", KEYS[1], ttl)
    return nextLimit
end